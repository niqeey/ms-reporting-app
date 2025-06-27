package com.smart.reporting.controller;

import com.smart.reporting.dto.LoginRequest;
import com.smart.reporting.dto.LoginResponse;
import com.smart.reporting.service.LoginService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class LoginController {

    @Autowired
    private LoginService loginService;

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest req) {
        LoginResponse resp = loginService.login(req);

    if (resp.isSuccess()) {
        return ResponseEntity.ok()
            .header("OrgId", resp.getOrgId() != null ? resp.getOrgId() : "")
            .header("UserName", resp.getUserName() != null ? resp.getUserName() : "")
            .header("SessionId", resp.getSessionId() != null ? resp.getSessionId() : "")
            .header("SessionExpiryTime", resp.getSessionExpiryTime() != null ? resp.getSessionExpiryTime().toString() : "")
            .header("Role", resp.getRole() != null ? resp.getRole() : "")
            .body(resp);
    } else {
        return ResponseEntity.status(401).body(resp);
    }
}
}