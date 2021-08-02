package com.cloud.cloudstorage.service;

import org.springframework.core.io.Resource;

import java.io.IOException;
import java.nio.file.Path;


public interface StorageService {

    void save(byte[] fileBytes, Path filepath) throws IOException;

    void deleteFile(Path filepath) throws IOException;

    Resource loadFile(Path filepath) throws IOException;

    String renameFile(Path filePath, String newFileName);
}
