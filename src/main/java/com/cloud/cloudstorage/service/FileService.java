package com.cloud.cloudstorage.service;

import com.cloud.cloudstorage.entity.FileEntity;
import org.springframework.core.io.Resource;

import java.io.IOException;
import java.util.List;

public interface FileService {

    void postFile(String filename, byte[] fileBytes, long fileSize) throws IOException;

    void deleteFile(String filename) throws IOException;

    Resource getFile(String filename) throws IOException;

    List<FileEntity> getList(int limit);

    void renameFile(String filename, String newFileName) throws IOException;
}
