package com.cloud.cloudstorage.props;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@Data
@ConfigurationProperties(prefix = "storage")
public class FileStoreProps {
    private String location;
}
