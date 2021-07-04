package com.cloud.cloudstorage.service;

import com.cloud.cloudstorage.entity.FileEntity;
import com.cloud.cloudstorage.entity.UserEntity;
import com.cloud.cloudstorage.props.FileStoreProps;
import com.cloud.cloudstorage.repository.FileRepository;
import com.cloud.cloudstorage.repository.UserRepository;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;


@Service
public class FileService {

    private final StorageService storageService;
    private final UserRepository userRepository;
    private final FileRepository fileRepository;

    protected final Log logger = LogFactory.getLog(this.getClass());

    private final Path rootLocation;

    public FileService(StorageService storageService, UserRepository userRepository, FileRepository fileRepository, FileStoreProps fileStorageProps) {
        this.storageService = storageService;
        this.userRepository = userRepository;
        this.fileRepository = fileRepository;
        this.rootLocation = Paths.get(fileStorageProps.getLocation());
    }

    @Transactional
    public void postFile(String filename, MultipartFile file) throws IOException {
        FileEntity fileEntity = FileEntity.builder()
                .filename(filename)
                .userEntity(getUserFromContext())
                .size(file.getSize())
                .filepath(getFilePath(filename).toAbsolutePath().toString())
                .build();
        storageService.save(file, getFilePath(filename).toAbsolutePath());
        fileRepository.save(fileEntity);
    }

    @Transactional
    public void deleteFile(String filename) throws IOException {
       storageService.deleteFile(getFilePath(filename));
       fileRepository.removeFileByFilenameAndUserEntity(filename, getUserFromContext());
    }

    public Resource getFile(String filename) throws IOException {
        return storageService.loadFile(getFilePath(filename));
    }

    @Transactional
    public List<FileEntity> getList(int limit) {
        UserEntity userEntity = getUserFromContext();
//        return fileRepository.findAllByUserEntity(userEntity, PageRequest.of(0, limit));
        return fileRepository.findAllByUserEntity_Login(userEntity.getLogin(), Sort.by("filename"));
    }

    private UserEntity getUserFromContext() {
        String login = SecurityContextHolder.getContext().getAuthentication().getName();
        return userRepository.findByLogin(login).orElseThrow(() -> new UsernameNotFoundException(login));
    }

    private Path getFilePath(String filename) {
        return rootLocation.resolve(getUserFromContext().getLogin()).resolve(filename);
    }
}
