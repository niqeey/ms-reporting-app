package com.smart.reporting.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
@Entity
@Table(name = "t_org")
public class TOrg {

    @Id
    @Column(length = 36)
    private String id;

    @Column(name = "org_name", length = 200, nullable = false)
    private String orgName;

    @Column(length = 200)
    private String owner;

    @Column(name = "is_active")
    private Boolean isActive;

    @Column(length = 100)
    private String ownercontact;

    @Column(length = 100)
    private String owneremail;

    @Column(name = "dt_create")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dtCreate;

    @Column(name = "dt_update")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dtUpdate;

    @Column(name = "alias", length = 100)
    private String alias;

}