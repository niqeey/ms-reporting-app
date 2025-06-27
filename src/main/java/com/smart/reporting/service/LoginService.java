package com.smart.reporting.service;

import com.smart.reporting.dto.LoginRequest;
import com.smart.reporting.dto.LoginResponse;
import com.smart.reporting.entity.TOrg;
import com.smart.reporting.entity.TOrgUser;
import com.smart.reporting.repository.TOrgRepository;
import com.smart.reporting.repository.TOrgUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;
import java.util.Date;

@Service
public class LoginService {

    @Autowired
    private TOrgUserRepository tOrgUserRepository;

    @Autowired
    private TOrgRepository tOrgRepository;

    public LoginResponse login(LoginRequest req) {
        Optional<TOrgUser> userOpt = tOrgUserRepository.findByUsernameAndPassword(req.getUsername(), req.getPassword());
        LoginResponse resp = new LoginResponse();

        if (userOpt.isPresent()) {
            TOrgUser user = userOpt.get();
            Optional<TOrg> orgOpt = tOrgRepository.findById(user.getOrgId());
            resp.setSuccess(true);
            resp.setMessage("Login successful! Welcome "+user.getFullName()+"!");
            resp.setOrgId(user.getOrgId());
            resp.setOrgName(orgOpt.map(TOrg::getOrgName).orElse(""));
            resp.setUserName(user.getUsername());
            resp.setRole(user.getRole());
            // Generate new session ID and expiry time
    String sessionId = UUID.randomUUID().toString();
    Date sessionExpiryTime = new Date(System.currentTimeMillis() + 2 * 60 * 60 * 1000); // 2 hours from now

    user.setSessionId(sessionId);
    user.setSessionExpiryTime(sessionExpiryTime);
    tOrgUserRepository.save(user);

    resp.setSessionId(sessionId);
    resp.setSessionExpiryTime(sessionExpiryTime);
        } else {
            resp.setSuccess(false);
            resp.setMessage("Invalid username or password");
        }
        return resp;
    }
}