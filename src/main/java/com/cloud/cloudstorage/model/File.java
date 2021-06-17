package com.cloud.cloudstorage.model;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class File {
    private String name;
    private Long size;
}
