package com.smart.reporting.entity;

import jakarta.persistence.*;
import java.util.Date;

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

    // Constructors
    public TOrg() {}

    public TOrg(String id, String orgName, String owner, Boolean isActive, String ownercontact, String owneremail, Date dtCreate, Date dtUpdate) {
        this.id = id;
        this.orgName = orgName;
        this.owner = owner;
        this.isActive = isActive;
        this.ownercontact = ownercontact;
        this.owneremail = owneremail;
        this.dtCreate = dtCreate;
        this.dtUpdate = dtUpdate;
    }

    // Getters and Setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getOrgName() { return orgName; }
    public void setOrgName(String orgName) { this.orgName = orgName; }

    public String getOwner() { return owner; }
    public void setOwner(String owner) { this.owner = owner; }

    public Boolean getIsActive() { return isActive; }
    public void setIsActive(Boolean isActive) { this.isActive = isActive; }

    public String getOwnercontact() { return ownercontact; }
    public void setOwnercontact(String ownercontact) { this.ownercontact = ownercontact; }

    public String getOwneremail() { return owneremail; }
    public void setOwneremail(String owneremail) { this.owneremail = owneremail; }

    public Date getDtCreate() { return dtCreate; }
    public void setDtCreate(Date dtCreate) { this.dtCreate = dtCreate; }

    public Date getDtUpdate() { return dtUpdate; }
    public void setDtUpdate(Date dtUpdate) { this.dtUpdate = dtUpdate; }
}