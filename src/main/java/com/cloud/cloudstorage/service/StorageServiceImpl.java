package com.cloud.cloudstorage.service;

import lombok.AllArgsConstructor;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Service
@AllArgsConstructor
public class StorageServiceImpl implements StorageService {

    @Override
    public void save(byte[] fileBytes, Path filepath) throws IOException {
        Files.createDirectories(filepath.getParent());
        Files.write(filepath, fileBytes);
    }

    @Override
    public void deleteFile(Path filepath) throws IOException {
        Files.deleteIfExists(filepath);
    }

    @Override
    public Resource loadFile(Path filepath) throws IOException {
        Resource resource = new FileSystemResource(filepath);
        if (resource.exists() || resource.isReadable()) {
            return resource;
        } else {
            throw new IOException("File \"" + filepath + "\" not found/loaded");
        }
    }

    @Override
    public String renameFile(Path filePath, String newFileName) {
        File file = filePath.toFile();
        File newFile = new File(filePath.resolveSibling(Paths.get(newFileName)).toString());
        if (file.renameTo(newFile))
            return newFile.getPath();
        else
            return null;
    }
}
