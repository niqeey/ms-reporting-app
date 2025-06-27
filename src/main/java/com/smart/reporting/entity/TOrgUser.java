package com.smart.reporting.entity;

import jakarta.persistence.*;
import java.util.Date;

@Entity
@Table(name = "t_org_user")
public class TOrgUser {

    @Id
    @Column(length = 36)
    private String id;

    @Column(length = 100, nullable = false)
    private String username;

    @Column(name = "org_id", length = 36, nullable = false)
    private String orgId;

    @Column(name = "session_id", length = 100)
    private String sessionId;

    @Column(name = "expiry_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date expiryDate;

    @Column(length = 100, nullable = false)
    private String password;

    @Column(name = "session_expiry_time")
    @Temporal(TemporalType.TIMESTAMP)
    private Date sessionExpiryTime;

    @Column(name = "FullName", length = 200)
    private String fullName;

    @Column(name = "role", length = 50)
    private String role;

    // Constructors
    public TOrgUser() {}

    public TOrgUser(String id, String username, String orgId, String sessionId, Date expiryDate, String password, Date sessionExpiryTime, String fullName) {
        this.id = id;
        this.username = username;
        this.orgId = orgId;
        this.sessionId = sessionId;
        this.expiryDate = expiryDate;
        this.password = password;
        this.sessionExpiryTime = sessionExpiryTime;
        this.fullName = fullName;
    }

    // Getters and Setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public String getOrgId() { return orgId; }
    public void setOrgId(String orgId) { this.orgId = orgId; }

    public String getSessionId() { return sessionId; }
    public void setSessionId(String sessionId) { this.sessionId = sessionId; }

    public Date getExpiryDate() { return expiryDate; }
    public void setExpiryDate(Date expiryDate) { this.expiryDate = expiryDate; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public Date getSessionExpiryTime() { return sessionExpiryTime; }
    public void setSessionExpiryTime(Date sessionExpiryTime) { this.sessionExpiryTime = sessionExpiryTime; }

    public String getFullName() { return fullName; }
    public void setFullName(String fullName) { this.fullName = fullName; }

    public String getRole() { return role; }
    public void setRole(String role) { this.role = role; }

}