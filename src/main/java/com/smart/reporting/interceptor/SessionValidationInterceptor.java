package com.smart.reporting.interceptor;

import com.smart.reporting.entity.TOrgUser;
import com.smart.reporting.repository.TOrgUserRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import java.util.Date;
import java.util.Optional;

@Component
public class SessionValidationInterceptor implements HandlerInterceptor {

    @Autowired
    private TOrgUserRepository userRepository;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        // Allow CORS preflight requests
        if ("OPTIONS".equalsIgnoreCase(request.getMethod())) {
            return true;
        }

        // Allow login endpoint and static resources without session validation
        String requestURI = request.getRequestURI();
        if (requestURI.contains("/auth/login") || 
            requestURI.contains("/actuator") ||
            requestURI.contains("/public/leaderboard") ||
            requestURI.contains("/race/categories") ||
            requestURI.endsWith("/favicon.ico")) {
            return true;
        }

        // Extract session headers
        String username = request.getHeader("username");
        String sessionId = request.getHeader("sessionId");
        String sessionExpiryTimeStr = request.getHeader("sessionExpiryTime");

        // Validate headers exist
        if (username == null || sessionId == null || sessionExpiryTimeStr == null) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getWriter().write("{\"error\":\"Missing authentication headers\",\"requireLogin\":true}");
            response.setContentType("application/json");
            return false;
        }

        // Validate session in database
        Optional<TOrgUser> userOpt = userRepository.findByUsername(username);
        if (userOpt.isEmpty()) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getWriter().write("{\"error\":\"User not found\",\"requireLogin\":true}");
            response.setContentType("application/json");
            return false;
        }

        TOrgUser user = userOpt.get();

        // Check if session ID matches
        if (!sessionId.equals(user.getSessionId())) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getWriter().write("{\"error\":\"Invalid session\",\"requireLogin\":true}");
            response.setContentType("application/json");
            return false;
        }

        // Check if session has expired
        if (user.getSessionExpiryTime() == null || user.getSessionExpiryTime().before(new Date())) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getWriter().write("{\"error\":\"Session expired\",\"requireLogin\":true}");
            response.setContentType("application/json");
            return false;
        }

        // Session is valid
        return true;
    }
}
