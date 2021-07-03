package com.cloud.cloudstorage.controller;

import com.cloud.cloudstorage.entity.FileEntity;
import com.cloud.cloudstorage.service.FileService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@AllArgsConstructor
@RestController
@RequestMapping(path = "/list")
public class ListController {

    private final FileService fileService;

    @GetMapping
    List<FileEntity> getList(@RequestParam int limit) {
        return fileService.getList(limit);
    }
}
