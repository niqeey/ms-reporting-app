package com.smart.reporting.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.smart.reporting.dto.LoginRequest;
import com.smart.reporting.dto.LoginResponse;
import com.smart.reporting.service.LoginService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Date;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(LoginController.class)
class LoginControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private LoginService loginService;

    private LoginRequest validLoginRequest;
    private LoginResponse successResponse;
    private LoginResponse failureResponse;

    @BeforeEach
    void setUp() {
        validLoginRequest = new LoginRequest();
        validLoginRequest.setUsername("testuser");
        validLoginRequest.setPassword("password123");

        successResponse = new LoginResponse();
        successResponse.setSuccess(true);
        successResponse.setMessage("Login successful! Welcome Test User!");
        successResponse.setOrgId("org123");
        successResponse.setOrgName("Test Organization");
        successResponse.setUserName("testuser");
        successResponse.setSessionId("session123");
        successResponse.setSessionExpiryTime(new Date(System.currentTimeMillis() + 7200000)); // 2 hours
        successResponse.setRole("admin");

        failureResponse = new LoginResponse();
        failureResponse.setSuccess(false);
        failureResponse.setMessage("Invalid username or password");
    }

    @Test
    void testSuccessfulLogin() throws Exception {
        when(loginService.login(any(LoginRequest.class))).thenReturn(successResponse);

        mockMvc.perform(post("/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(validLoginRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.message").value("Login successful! Welcome Test User!"))
                .andExpect(jsonPath("$.orgId").value("org123"))
                .andExpect(jsonPath("$.userName").value("testuser"))
                .andExpect(jsonPath("$.sessionId").value("session123"))
                .andExpect(jsonPath("$.role").value("admin"))
                .andExpect(header().exists("OrgId"))
                .andExpect(header().exists("SessionId"))
                .andExpect(header().exists("SessionExpiryTime"))
                .andExpect(header().exists("Role"));
    }

    @Test
    void testFailedLogin() throws Exception {
        when(loginService.login(any(LoginRequest.class))).thenReturn(failureResponse);

        mockMvc.perform(post("/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(validLoginRequest)))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.message").value("Invalid username or password"));
    }

    @Test
    void testLoginWithMissingUsername() throws Exception {
        LoginRequest invalidRequest = new LoginRequest();
        invalidRequest.setPassword("password123");

        mockMvc.perform(post("/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(invalidRequest)))
                .andExpect(status().is4xxClientError());
    }

    @Test
    void testLoginWithMissingPassword() throws Exception {
        LoginRequest invalidRequest = new LoginRequest();
        invalidRequest.setUsername("testuser");

        mockMvc.perform(post("/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(invalidRequest)))
                .andExpect(status().is4xxClientError());
    }
}
