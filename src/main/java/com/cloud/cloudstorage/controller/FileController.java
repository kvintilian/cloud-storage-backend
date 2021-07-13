package com.cloud.cloudstorage.controller;

import com.cloud.cloudstorage.model.ErrorResponse;
import com.cloud.cloudstorage.model.NewFileName;
import com.cloud.cloudstorage.service.FileService;
import com.sun.istack.NotNull;
import lombok.AllArgsConstructor;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@AllArgsConstructor
@RestController
@RequestMapping(value = "/file")
public class FileController {

    private static final String FILE_NAME = "filename";
    private static final String FILE = "file";

    private final FileService fileService;

    protected final Log logger = LogFactory.getLog(this.getClass());

    @PostMapping()
    public ResponseEntity<?> postFile(@RequestParam(FILE_NAME) String filename,
                                      @RequestPart(FILE) @NotNull MultipartFile file) {
        try {
            fileService.postFile(filename, file);
        } catch (IOException e) {
            return ResponseEntity.badRequest().body(new ErrorResponse(e.getMessage(), -32002));
        }
        return ResponseEntity.ok().body(null);
    }

    @DeleteMapping()
    public ResponseEntity<?> deleteFile(@RequestParam(FILE_NAME) String filename) {
        try {
            fileService.deleteFile(filename);
        } catch (IOException e) {
            return ResponseEntity.badRequest().body(new ErrorResponse(e.getMessage(), -32003));
        }
        return ResponseEntity.ok().body(null);
    }

    @GetMapping(produces = MediaType.ALL_VALUE)
    public ResponseEntity<?> getFile(@RequestParam(FILE_NAME) String filename) {
        Resource resource = null;
        try {
            resource = fileService.getFile(filename);
            logger.info("getFile.fileSize=" + resource.getFile().length());
            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION,
                            "attachment; filename=\"" + resource.getFilename() + "\"")
                    .contentLength(resource.getFile().length())
                    .body(resource);
        } catch (IOException e) {
            return ResponseEntity.badRequest().body(new ErrorResponse(e.getMessage(), -32004));
        }
    }

    @PutMapping()
    public ResponseEntity<?> updateFile(@RequestParam(FILE_NAME) String filename, @RequestBody NewFileName newFileName) {
        try {
            fileService.renameFile(filename, newFileName.getName());
        } catch (IOException e) {
            return ResponseEntity.badRequest().body(new ErrorResponse(e.getMessage(), -32005));
        }
        return ResponseEntity.ok().body(null);
    }
}
