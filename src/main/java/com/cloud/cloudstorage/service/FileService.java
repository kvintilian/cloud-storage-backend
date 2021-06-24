package com.cloud.cloudstorage.service;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.core.io.Resource;

@Service
public class FileService {

    public void postFile(String filename, MultipartFile file) {
    }

    public void deleteFile(String filename) {
    }

    public Resource getFile(String filename) {
        return null;
    }
}
