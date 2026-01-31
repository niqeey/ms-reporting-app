package com.smart.reporting.interceptor;

import com.smart.reporting.entity.TOrgUser;
import com.smart.reporting.repository.TOrgUserRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Date;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SessionValidationInterceptorTest {

    @Mock
    private TOrgUserRepository userRepository;

    @Mock
    private HttpServletRequest request;

    @Mock
    private HttpServletResponse response;

    @InjectMocks
    private SessionValidationInterceptor interceptor;

    private StringWriter stringWriter;
    private PrintWriter writer;
    private TOrgUser validUser;

    @BeforeEach
    void setUp() throws Exception {
        stringWriter = new StringWriter();
        writer = new PrintWriter(stringWriter);
        when(response.getWriter()).thenReturn(writer);

        validUser = new TOrgUser();
        validUser.setUsername("testuser");
        validUser.setSessionId("session123");
        validUser.setSessionExpiryTime(new Date(System.currentTimeMillis() + 7200000)); // 2 hours from now
    }

    @Test
    void testPreHandleOptionsRequest() throws Exception {
        when(request.getMethod()).thenReturn("OPTIONS");

        boolean result = interceptor.preHandle(request, response, null);

        assertTrue(result);
        verify(response, never()).setStatus(anyInt());
    }

    @Test
    void testPreHandleLoginEndpoint() throws Exception {
        when(request.getMethod()).thenReturn("POST");
        when(request.getRequestURI()).thenReturn("/auth/login");

        boolean result = interceptor.preHandle(request, response, null);

        assertTrue(result);
        verify(response, never()).setStatus(anyInt());
    }

    @Test
    void testPreHandleMissingHeaders() throws Exception {
        when(request.getMethod()).thenReturn("POST");
        when(request.getRequestURI()).thenReturn("/org/event/list");
        when(request.getHeader("username")).thenReturn(null);
        when(request.getHeader("sessionId")).thenReturn(null);
        when(request.getHeader("sessionExpiryTime")).thenReturn(null);

        boolean result = interceptor.preHandle(request, response, null);

        assertFalse(result);
        verify(response).setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        writer.flush();
        assertTrue(stringWriter.toString().contains("Missing authentication headers"));
    }

    @Test
    void testPreHandleUserNotFound() throws Exception {
        when(request.getMethod()).thenReturn("POST");
        when(request.getRequestURI()).thenReturn("/org/event/list");
        when(request.getHeader("username")).thenReturn("nonexistent");
        when(request.getHeader("sessionId")).thenReturn("session123");
        when(request.getHeader("sessionExpiryTime")).thenReturn("2026-01-31T12:00:00");
        when(userRepository.findByUsername(anyString())).thenReturn(Optional.empty());

        boolean result = interceptor.preHandle(request, response, null);

        assertFalse(result);
        verify(response).setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        writer.flush();
        assertTrue(stringWriter.toString().contains("User not found"));
    }

    @Test
    void testPreHandleInvalidSessionId() throws Exception {
        when(request.getMethod()).thenReturn("POST");
        when(request.getRequestURI()).thenReturn("/org/event/list");
        when(request.getHeader("username")).thenReturn("testuser");
        when(request.getHeader("sessionId")).thenReturn("wrongsession");
        when(request.getHeader("sessionExpiryTime")).thenReturn("2026-01-31T12:00:00");
        when(userRepository.findByUsername(anyString())).thenReturn(Optional.of(validUser));

        boolean result = interceptor.preHandle(request, response, null);

        assertFalse(result);
        verify(response).setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        writer.flush();
        assertTrue(stringWriter.toString().contains("Invalid session"));
    }

    @Test
    void testPreHandleExpiredSession() throws Exception {
        TOrgUser expiredUser = new TOrgUser();
        expiredUser.setUsername("testuser");
        expiredUser.setSessionId("session123");
        expiredUser.setSessionExpiryTime(new Date(System.currentTimeMillis() - 3600000)); // 1 hour ago

        when(request.getMethod()).thenReturn("POST");
        when(request.getRequestURI()).thenReturn("/org/event/list");
        when(request.getHeader("username")).thenReturn("testuser");
        when(request.getHeader("sessionId")).thenReturn("session123");
        when(request.getHeader("sessionExpiryTime")).thenReturn("2026-01-31T12:00:00");
        when(userRepository.findByUsername(anyString())).thenReturn(Optional.of(expiredUser));

        boolean result = interceptor.preHandle(request, response, null);

        assertFalse(result);
        verify(response).setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        writer.flush();
        assertTrue(stringWriter.toString().contains("Session expired"));
    }

    @Test
    void testPreHandleValidSession() throws Exception {
        when(request.getMethod()).thenReturn("POST");
        when(request.getRequestURI()).thenReturn("/org/event/list");
        when(request.getHeader("username")).thenReturn("testuser");
        when(request.getHeader("sessionId")).thenReturn("session123");
        when(request.getHeader("sessionExpiryTime")).thenReturn("2026-01-31T12:00:00");
        when(userRepository.findByUsername(anyString())).thenReturn(Optional.of(validUser));

        boolean result = interceptor.preHandle(request, response, null);

        assertTrue(result);
        verify(response, never()).setStatus(anyInt());
    }
}
