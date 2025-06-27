package com.smart.reporting.dto;

import java.util.Date;

public class LoginResponse {
    private boolean success;
    private String message;
    private String orgId;
    private String orgName;
    private String userName;
    private String sessionId;
    private Date sessionExpiryTime;
    private String role;

    public boolean isSuccess() { return success; }
    public void setSuccess(boolean success) { this.success = success; }

    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }

    public String getOrgId() { return orgId; }
    public void setOrgId(String orgId) { this.orgId = orgId; }

    public String getOrgName() { return orgName; }
    public void setOrgName(String orgName) { this.orgName = orgName; }

    public String getUserName() { return userName; }
    public void setUserName(String userName) { this.userName = userName; }

    public String getSessionId() { return sessionId; }
    public void setSessionId(String sessionId) { this.sessionId = sessionId; }

    public Date getSessionExpiryTime() { return sessionExpiryTime; }
    public void setSessionExpiryTime(Date sessionExpiryTime) { this.sessionExpiryTime = sessionExpiryTime; }

    public String getRole() { return role; }
    public void setRole(String role) { this.role = role; }

}