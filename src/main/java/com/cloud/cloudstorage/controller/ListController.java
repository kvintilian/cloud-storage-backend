package com.cloud.cloudstorage.controller;

import com.cloud.cloudstorage.entity.FileEntity;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@AllArgsConstructor
@RestController
@RequestMapping(path = "/cloud/list")
public class ListController {

    @GetMapping
    List<FileEntity> getList(@RequestParam int limit) {
        return null;
//        return locationService.getList(limit).stream()
//                .map(boxFile -> new BoxFileDto(boxFile.getName(), boxFile.getSize()))
//                .collect(Collectors.toList());
    }
}
