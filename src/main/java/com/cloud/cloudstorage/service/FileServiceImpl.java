package com.cloud.cloudstorage.service;

import com.cloud.cloudstorage.entity.FileEntity;
import com.cloud.cloudstorage.entity.UserEntity;
import com.cloud.cloudstorage.props.FileStoreProps;
import com.cloud.cloudstorage.repository.FileRepository;
import com.cloud.cloudstorage.repository.UserRepository;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

@Service
public class FileServiceImpl implements FileService {

    private final StorageService storageService;
    private final UserRepository userRepository;
    private final FileRepository fileRepository;

    private final Path rootLocation;

    public FileServiceImpl(StorageService storageService, UserRepository userRepository, FileRepository fileRepository, FileStoreProps fileStorageProps) {
        this.storageService = storageService;
        this.userRepository = userRepository;
        this.fileRepository = fileRepository;
        this.rootLocation = Paths.get(fileStorageProps.getLocation());
    }

    @Transactional
    @Override
    public void postFile(String filename, byte[] fileBytes, long fileSize) throws IOException {
        FileEntity fileEntity = FileEntity.builder()
                .filename(filename)
                .userEntity(getUserFromContext())
                .size(fileSize)
                .filepath(getFilePath(filename).toAbsolutePath().toString())
                .build();
        storageService.save(fileBytes, getFilePath(filename).toAbsolutePath());
        fileRepository.save(fileEntity);
    }

    @Transactional
    @Override
    public void deleteFile(String filename) throws IOException {
        storageService.deleteFile(getFilePath(filename));
        fileRepository.removeFileByFilenameAndUserEntity(filename, getUserFromContext());
    }

    @Override
    public Resource getFile(String filename) throws IOException {
        return storageService.loadFile(getFilePath(filename));
    }

    @Override
    public List<FileEntity> getList(int limit) {
        UserEntity userEntity = getUserFromContext();
        // return fileRepository.findAllByUserEntity(userEntity, PageRequest.of(0, limit)); // not supported on the frontend
        return fileRepository.findAllByUserEntity_Login(userEntity.getLogin(), Sort.by("filename"));
    }

    private UserEntity getUserFromContext() {
        String login = SecurityContextHolder.getContext().getAuthentication().getName();
        return userRepository.findByLogin(login).orElseThrow(() -> new UsernameNotFoundException(login));
    }

    private Path getFilePath(String filename) {
        return rootLocation.resolve(getUserFromContext().getLogin()).resolve(filename);
    }

    @Override
    public void renameFile(String filename, String newFileName) throws IOException {
        FileEntity fileEntity = fileRepository.findByFilenameAndUserEntity(filename, getUserFromContext());
        String newFilePath = storageService.renameFile(getFilePath(filename), newFileName);
        if (newFilePath != null) {
            fileEntity.setFilename(newFileName);
            fileEntity.setFilepath(newFilePath);
            fileRepository.saveAndFlush(fileEntity);
        } else {
            throw new IOException(String.format("Error rename fille \"%s\" to \"%s\"", filename, newFileName));
        }
    }
}
