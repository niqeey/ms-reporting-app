package com.smart.reporting.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
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


}