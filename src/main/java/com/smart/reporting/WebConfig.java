package com.smart.reporting;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        System.out.println("Configuring CORS...");
        registry.addMapping("/**") // Allow all endpoints
                .allowedOrigins("https://www.mypacetracker.com", "http://192.168.1.184:7755", "http://localhost:7755") // Allow calls from these origins
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS") // Allow these HTTP methods
                .allowedHeaders("*") // Allow all headers
                .exposedHeaders("OrgId", "UserName", "SessionId", "SessionExpiryTime", "Role") // Expose custom headers
                .allowCredentials(true); // Allow cookies and credentials
    }
}
