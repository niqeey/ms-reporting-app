package com.smart.reporting.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.util.Date;

@Data
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

    @Column(name = "weather" , length = 100)
    private String weather;

    @Column(name = "archived")
    private Boolean archived = false;

    // Constructors
    public TEvent() {}

    public TEvent(String id, String name, Date eventDt, String location, String country, String weather) {
        this.id = id;
        this.name = name;
        this.eventDt = eventDt;
        this.location = location;
        this.country = country;
        this.weather = weather;
        this.archived = false;
    }
}