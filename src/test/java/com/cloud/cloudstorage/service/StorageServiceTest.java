package com.cloud.cloudstorage.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.Resource;
import org.springframework.mock.web.MockMultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class StorageServiceTest {

    public static final String FILE_TXT = "file.txt";
    public static final String TEST_STRING = "test string";
    public static final String NEW_FILE_NAME_TXT = "NewFileName.txt";

    @Autowired
    StorageService storageService;

    @TempDir
    Path tempDir;

    @Test
    void saveSuccess() throws IOException {
        MockMultipartFile mockMultipartFile = new MockMultipartFile(FILE_TXT, TEST_STRING.getBytes(StandardCharsets.UTF_8));
        storageService.save(mockMultipartFile, tempDir.resolve(FILE_TXT));
        assertAll(
                () -> assertTrue(Files.exists(tempDir.resolve(FILE_TXT))),
                () -> assertLinesMatch(Collections.singletonList(TEST_STRING), Files.readAllLines(tempDir.resolve(FILE_TXT)))
        );
    }

    @Test
    void loadSuccess() throws IOException {
        File tempFile = createFile();
        Resource resource = storageService.loadFile(tempFile.toPath());
        assertAll(
                () -> assertTrue(resource.exists()),
                () -> assertTrue(resource.isReadable()),
                () -> assertLinesMatch(Collections.singletonList(TEST_STRING), Files.readAllLines(resource.getFile().toPath()))
        );
    }

//    @Test
//    void renameSuccess() throws IOException {
//        File tempFile = createFile();
//        storageService.renameFile(tempFile.toPath(), NEW_FILE_NAME_TXT);
//        assertTrue(Files.exists(tempDir.resolve(NEW_FILE_NAME_TXT)));
//    }

    @Test
    void deleteSuccess() throws IOException {
        File tempFile = createFile();
        storageService.deleteFile(tempFile.toPath());
        assertFalse(Files.exists(tempFile.toPath()));
    }

    private File createFile() throws IOException {
        File tempFile = new File(tempDir.toFile(), FILE_TXT);
        Files.write(tempFile.toPath(), TEST_STRING.getBytes());
        return tempFile;
    }

}