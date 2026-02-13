package com.smart.reporting.dto;

import java.util.Date;

public class EventResponse {
    private String id;
    private String name;
    private Date eventDt;
    private String location;
    private String country;
    private Boolean archived;

    // Getters and setters
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

    public Boolean getArchived() { return archived; }
    public void setArchived(Boolean archived) { this.archived = archived; }


    // Constructor
    public EventResponse() {}

    public EventResponse(String id, String name, Date eventDt, String location, String country) {
        this.id = id;
        this.name = name;
        this.eventDt = eventDt;
        this.location = location;
        this.country = country;
        this.archived = false;
    }

    public EventResponse(String id, String name, Date eventDt, String location, String country, Boolean archived) {
        this.id = id;
        this.name = name;
        this.eventDt = eventDt;
        this.location = location;
        this.country = country;
        this.archived = archived;
    }
}