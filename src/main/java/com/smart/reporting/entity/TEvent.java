package com.smart.reporting.entity;

import jakarta.persistence.*;
import java.util.Date;

@Entity
@Table(name = "t_event")
public class TEvent {

    @Id
    @Column(length = 36)
    private String id;

    @Column(length = 200, nullable = false)
    private String name;

    @Column(name = "event_dt")
    @Temporal(TemporalType.DATE)
    private Date eventDt;

    @Column(length = 100)
    private String location;

    @Column(length = 100, nullable = false)
    private String country;

    // Constructors
    public TEvent() {}

    public TEvent(String id, String name, Date eventDt, String location, String country) {
        this.id = id;
        this.name = name;
        this.eventDt = eventDt;
        this.location = location;
        this.country = country;
    }

    // Getters and Setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public Date getEventDt() { return eventDt; }
    public void setEventDt(Date eventDt) { this.eventDt = eventDt; }

    public String getLocation() { return location; }
    public void setLocation(String location) { this.location = location; }

    public String getCountry() { return country; }
    public void setCountry(String country) { this.country = country; }
}