package com.smart.reporting.entity;

import jakarta.persistence.*;

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

    // Constructors
    public TOrgEvent() {}

    public TOrgEvent(String id, String orgId, String eventId) {
        this.id = id;
        this.orgId = orgId;
        this.eventId = eventId;
    }

    // Getters and Setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getOrgId() { return orgId; }
    public void setOrgId(String orgId) { this.orgId = orgId; }

    public String getEventId() { return eventId; }
    public void setEventId(String eventId) { this.eventId = eventId; }
}