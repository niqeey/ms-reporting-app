package com.smart.reporting.dto;

public class EventCategoryResponse {
    private String eventId;
    private String eventName;
    private Long catId;
    private String cat;
    private String name;

    public EventCategoryResponse() {}

    public EventCategoryResponse(String eventId, String eventName, Long catId, String cat, String name) {
        this.eventId = eventId;
        this.eventName = eventName;
        this.catId = catId;
        this.cat = cat;
        this.name = name;
    }

    public String getEventId() {
        return eventId;
    }
    

    public void setEventId(String eventId) {
        this.eventId = eventId;
    }

    public String getEventName() {
        return eventName;
    }
    public void setEventName(String eventName) {
        this.eventName = eventName;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
    public Long getCatId() {
        return catId;
    }
    public void setCatId(Long catId) {
        this.catId = catId;
    }
    public String getCat() {
        return cat;
    }
}
