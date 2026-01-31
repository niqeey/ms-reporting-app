package com.smart.reporting.service;

import com.smart.reporting.dto.LoginRequest;
import com.smart.reporting.dto.LoginResponse;
import com.smart.reporting.entity.TOrg;
import com.smart.reporting.entity.TOrgUser;
import com.smart.reporting.repository.TOrgRepository;
import com.smart.reporting.repository.TOrgUserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Date;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class LoginServiceTest {

    @Mock
    private TOrgUserRepository userRepository;

    @Mock
    private TOrgRepository orgRepository;

    @InjectMocks
    private LoginService loginService;

    private TOrgUser testUser;
    private TOrg testOrg;
    private LoginRequest loginRequest;

    @BeforeEach
    void setUp() {
        testUser = new TOrgUser();
        testUser.setId("user123");
        testUser.setUsername("testuser");
        testUser.setPassword("password123");
        testUser.setOrgId("org123");
        testUser.setFullName("Test User");
        testUser.setRole("admin");

        testOrg = new TOrg();
        testOrg.setId("org123");
        testOrg.setOrgName("Test Organization");

        loginRequest = new LoginRequest();
        loginRequest.setUsername("testuser");
        loginRequest.setPassword("password123");
    }

    @Test
    void testSuccessfulLogin() {
        when(userRepository.findByUsernameAndPassword(anyString(), anyString()))
                .thenReturn(Optional.of(testUser));
        when(orgRepository.findById(anyString()))
                .thenReturn(Optional.of(testOrg));
        when(userRepository.save(any(TOrgUser.class)))
                .thenReturn(testUser);

        LoginResponse response = loginService.login(loginRequest);

        assertTrue(response.isSuccess());
        assertEquals("Login successful! Welcome Test User!", response.getMessage());
        assertEquals("org123", response.getOrgId());
        assertEquals("Test Organization", response.getOrgName());
        assertEquals("testuser", response.getUserName());
        assertEquals("admin", response.getRole());
        assertNotNull(response.getSessionId());
        assertNotNull(response.getSessionExpiryTime());
        
        verify(userRepository, times(1)).save(any(TOrgUser.class));
    }

    @Test
    void testFailedLoginInvalidCredentials() {
        when(userRepository.findByUsernameAndPassword(anyString(), anyString()))
                .thenReturn(Optional.empty());

        LoginResponse response = loginService.login(loginRequest);

        assertFalse(response.isSuccess());
        assertEquals("Invalid username or password", response.getMessage());
        assertNull(response.getOrgId());
        assertNull(response.getSessionId());
        
        verify(userRepository, never()).save(any(TOrgUser.class));
    }

    @Test
    void testLoginSessionGeneration() {
        when(userRepository.findByUsernameAndPassword(anyString(), anyString()))
                .thenReturn(Optional.of(testUser));
        when(orgRepository.findById(anyString()))
                .thenReturn(Optional.of(testOrg));
        when(userRepository.save(any(TOrgUser.class)))
                .thenReturn(testUser);

        LoginResponse response = loginService.login(loginRequest);

        assertNotNull(response.getSessionId());
        assertNotNull(response.getSessionExpiryTime());
        
        // Verify session expiry is approximately 2 hours from now
        long expectedExpiry = System.currentTimeMillis() + (2 * 60 * 60 * 1000);
        long actualExpiry = response.getSessionExpiryTime().getTime();
        long diff = Math.abs(expectedExpiry - actualExpiry);
        
        // Allow 5 second difference for test execution time
        assertTrue(diff < 5000, "Session expiry time should be approximately 2 hours from now");
    }

    @Test
    void testLoginWithOrgNotFound() {
        when(userRepository.findByUsernameAndPassword(anyString(), anyString()))
                .thenReturn(Optional.of(testUser));
        when(orgRepository.findById(anyString()))
                .thenReturn(Optional.empty());
        when(userRepository.save(any(TOrgUser.class)))
                .thenReturn(testUser);

        LoginResponse response = loginService.login(loginRequest);

        assertTrue(response.isSuccess());
        assertEquals("", response.getOrgName());
    }
}
