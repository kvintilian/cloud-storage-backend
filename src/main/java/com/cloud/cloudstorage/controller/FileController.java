package com.cloud.cloudstorage.controller;

import com.cloud.cloudstorage.service.FileService;
import com.sun.istack.NotNull;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping(value = "/cloud/file")
public class FileController {

    private static final String FILE_NAME = "filename";
    private static final String FILE = "file";

    private final FileService fileService;

    public FileController(FileService fileService) {
        this.fileService = fileService;
    }

    @PostMapping()
    public ResponseEntity<?> postFile(@RequestParam(FILE_NAME) String filename,
                                      @RequestPart(FILE) @NotNull MultipartFile file) {
        fileService.postFile(filename, file);
        return ResponseEntity.ok().body(null);
    }

    @DeleteMapping()
    public ResponseEntity<?> deleteFile(@RequestParam(FILE_NAME) String filename) {
        fileService.deleteFile(filename);
        return ResponseEntity.ok().body(null);
    }

    @GetMapping()
    public ResponseEntity<?> getFile(@RequestParam(FILE_NAME) String filename) throws IOException {
        Resource resource = fileService.getFile(filename);
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION,
                        "attachment; filename=\"" + resource.getFilename() + "\"")
                .contentLength(resource.getFile().length())
                .body(resource);
    }
}
