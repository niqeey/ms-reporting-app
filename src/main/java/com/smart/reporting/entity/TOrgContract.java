package com.smart.reporting.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
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
}