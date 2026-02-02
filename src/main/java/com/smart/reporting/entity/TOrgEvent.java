package com.smart.reporting.entity;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "t_org_event")
public class TOrgEvent {

    @Id
    @Column(length = 36)
    private String id;

    @Column(name = "org_id", length = 36, nullable = false)
    private String orgId;

    @Column(name = "event_id", length = 36, nullable = false)
    private String eventId;
}