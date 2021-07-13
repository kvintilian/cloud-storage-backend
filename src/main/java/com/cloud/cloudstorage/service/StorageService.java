package com.cloud.cloudstorage.service;

import com.cloud.cloudstorage.entity.FileEntity;
import com.cloud.cloudstorage.entity.UserEntity;
import com.cloud.cloudstorage.repository.FileRepository;
import lombok.AllArgsConstructor;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Service
@AllArgsConstructor
public class StorageService {

    private final FileRepository fileRepository;

    public void save(MultipartFile file, Path filepath) throws IOException {
        Files.createDirectories(filepath.getParent());
        Files.write(filepath, file.getBytes());
    }

    public void deleteFile(Path filepath) throws IOException {
        Files.deleteIfExists(filepath);
    }

    public Resource loadFile(Path filepath) throws IOException {
        Resource resource = new FileSystemResource(filepath);
        if (resource.exists() || resource.isReadable()) {
            return resource;
        } else {
            throw new IOException("File not load");
        }
    }

    public void renameFile(Path filePath, String newFileName, UserEntity userFromContext) {
        FileEntity fileEntity = fileRepository.findByFilenameAndUserEntity(filePath.getFileName().toString(), userFromContext);
        fileEntity.setFilename(newFileName);
        File file = filePath.toFile();
        File newFile = new File(filePath.resolve(Paths.get(newFileName)).toString());
        file.renameTo(newFile);
    }
}
