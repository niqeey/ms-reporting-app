package com.smart.reporting.entity;

import jakarta.persistence.*;
import java.util.Date;

@Entity
@Table(name = "t_org_contract")
public class TOrgContract {

    @Id
    @Column(length = 36)
    private String id;

    @Column(name = "org_id", length = 36, nullable = false)
    private String orgId;

    @Column(length = 100, nullable = false)
    private String description;

    @Column(name = "contract_start_date", nullable = false)
    @Temporal(TemporalType.DATE)
    private Date contractStartDate;

    @Column(name = "contract_end_date", nullable = false)
    @Temporal(TemporalType.DATE)
    private Date contractEndDate;

    @Column(name = "billing_duration", length = 100, nullable = false)
    private String billingDuration;

    @Column(length = 100, nullable = false)
    private String status;

    @Column(name = "is_active", nullable = false)
    private Boolean isActive;

    // Constructors
    public TOrgContract() {}

    // Getters and Setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getOrgId() { return orgId; }
    public void setOrgId(String orgId) { this.orgId = orgId; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public Date getContractStartDate() { return contractStartDate; }
    public void setContractStartDate(Date contractStartDate) { this.contractStartDate = contractStartDate; }

    public Date getContractEndDate() { return contractEndDate; }
    public void setContractEndDate(Date contractEndDate) { this.contractEndDate = contractEndDate; }

    public String getBillingDuration() { return billingDuration; }
    public void setBillingDuration(String billingDuration) { this.billingDuration = billingDuration; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public Boolean getIsActive() { return isActive; }
    public void setIsActive(Boolean isActive) { this.isActive = isActive; }
}