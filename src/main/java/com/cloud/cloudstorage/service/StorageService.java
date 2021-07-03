package com.cloud.cloudstorage.service;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

@Service
public class StorageService {

    public void save(MultipartFile file, String filename, Path filepath) throws IOException {
        Files.createDirectories(filepath.getParent());
        Files.write(filepath, file.getBytes());
    }

    public void deleteFile(Path filepath) throws IOException {
        Files.deleteIfExists(filepath);
    }
}
