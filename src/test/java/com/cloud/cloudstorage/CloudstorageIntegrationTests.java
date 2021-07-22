package com.cloud.cloudstorage;

import com.cloud.cloudstorage.model.JwtRequest;
import com.cloud.cloudstorage.model.JwtResponse;
import com.github.dockerjava.zerodep.shaded.org.apache.hc.client5.http.classic.methods.HttpPost;
import com.github.dockerjava.zerodep.shaded.org.apache.hc.client5.http.entity.mime.MultipartEntityBuilder;
import com.github.dockerjava.zerodep.shaded.org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import com.github.dockerjava.zerodep.shaded.org.apache.hc.client5.http.impl.classic.CloseableHttpResponse;
import com.github.dockerjava.zerodep.shaded.org.apache.hc.client5.http.impl.classic.HttpClients;
import com.github.dockerjava.zerodep.shaded.org.apache.hc.core5.http.ContentType;
import com.github.dockerjava.zerodep.shaded.org.apache.hc.core5.http.HttpEntity;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.io.TempDir;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.shaded.com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.assertAll;

@Testcontainers(disabledWithoutDocker = true)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class CloudstorageIntegrationTests {

    @Autowired
    TestRestTemplate restTemplate;

    private static final String HOST = "http://localhost";
    private static final int PORT = 8081;
    private static final GenericContainer<?> cloudStorge = new GenericContainer<>("backdock").withExposedPorts(PORT);

    @TempDir
    Path tempDir;

    @BeforeAll
    public static void setUp() {
        cloudStorge.start();
    }

    @AfterAll
    public static void setDown() {
        cloudStorge.stop();
    }

    private static final String ENDPOINT_LOGIN = "/login";
    private static final String ENDPOINT_LOGOUT = "/logout";
    private static final String ENDPOINT_FILE = "/file";

    public static final String JOHN = "john";
    public static final String DOE = "doe";

    public static final String FILE_TXT = "file.txt";
    public static final String TEST_STRING = "test string";

    private static final String TOKEN_PREFIX = "Bearer ";
    private static String token;

    // Login
    @Test
    @Order(1)
    void loginSuccess() throws IOException {
        JwtRequest jwtRequest = new JwtRequest(JOHN, DOE);
        ResponseEntity<String> responseEntity = restTemplate.postForEntity(
                HOST + ":" + cloudStorge.getMappedPort(PORT) + ENDPOINT_LOGIN, jwtRequest, String.class);
        String response = responseEntity.getBody();
        JwtResponse jwtResponse = new ObjectMapper().readValue(response, JwtResponse.class);
        token = jwtResponse.getAuthToken();
        assertAll(
                () -> Assertions.assertEquals(HttpStatus.OK, responseEntity.getStatusCode()),
                () -> Assertions.assertNotNull(token)
        );
    }

    // Upload file
    @Test
    @Order(2)
    void uploadSuccess() throws Exception {
        File file = createTempFile();
        CloseableHttpClient httpClient = HttpClients.createDefault();
        HttpPost uploadFile = new HttpPost(HOST + ":" + cloudStorge.getMappedPort(PORT) + ENDPOINT_FILE);
        MultipartEntityBuilder builder = MultipartEntityBuilder.create()
                .addTextBody("filename", file.getName(), ContentType.TEXT_PLAIN)
                .addBinaryBody(
                        "file",
                        new FileInputStream(file),
                        ContentType.APPLICATION_OCTET_STREAM,
                        file.getName()
                );
        HttpEntity multipart = builder.build();
        uploadFile.setEntity(multipart);
        uploadFile.setHeader("auth-token", TOKEN_PREFIX + token);
        CloseableHttpResponse response = httpClient.execute(uploadFile);
        Assertions.assertEquals(HttpStatus.OK, HttpStatus.valueOf(response.getCode()));
    }

    // Logout
    @Test
    @Order(3)
    void logoutSuccess() {
        ResponseEntity<String> responseEntity = restTemplate.postForEntity(
                HOST + ":" + cloudStorge.getMappedPort(PORT) + ENDPOINT_LOGOUT,
                "", String.class);
        // Success redirect
        Assertions.assertEquals(HttpStatus.FOUND, responseEntity.getStatusCode());
    }

   File createTempFile() throws IOException {
        File tempFile = new File(tempDir.toFile(), FILE_TXT);
        Files.write(tempFile.toPath(), TEST_STRING.getBytes());
        return tempFile;
    }

}
