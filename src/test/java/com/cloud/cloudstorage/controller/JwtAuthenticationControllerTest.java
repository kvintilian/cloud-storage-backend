package com.cloud.cloudstorage.controller;

import com.cloud.cloudstorage.model.JwtRequest;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class JwtAuthenticationControllerTest {

    public static final String LIST_LIMIT_3 = "/list?limit=3";
    public static final String JOHN = "john";
    public static final String DOE = "doe";
    public static final String LOGIN = "/login";
    public static final String NOTJHON = "notjhon";

    @Autowired
    MockMvc mockMvc;

    @Test
    void successfulLogin() throws Exception {
        JwtRequest jwtRequest = new JwtRequest(JOHN, DOE);
        ObjectMapper objectMapper = new ObjectMapper();
        final String json = objectMapper.writeValueAsString(jwtRequest);
        mockMvc.perform(post(LOGIN).contentType(MediaType.APPLICATION_JSON)
                .content(json)).andExpect(status().isOk());
    }

    @Test
    void badRequestLogin() throws Exception {
        JwtRequest jwtRequest = new JwtRequest(NOTJHON, DOE);
        ObjectMapper objectMapper = new ObjectMapper();
        final String json = objectMapper.writeValueAsString(jwtRequest);
        mockMvc.perform(post(LOGIN).contentType(MediaType.APPLICATION_JSON)
                .content(json)).andExpect(status().isBadRequest());
    }


    @Test
    @WithMockUser(username = JOHN, password = DOE)
    void authorizedRequestList() throws Exception {
        mockMvc.perform(get(LIST_LIMIT_3)).andExpect(status().isOk());
    }

    @Test
    void unauthorizedRequestList() throws Exception {
        mockMvc.perform(get(LIST_LIMIT_3)).andExpect(status().isUnauthorized());
    }


}