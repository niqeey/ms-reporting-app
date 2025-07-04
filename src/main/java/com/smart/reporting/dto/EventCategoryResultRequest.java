package com.smart.reporting.dto;

public class EventCategoryResultRequest {
    private String eventId;
    private String category;

    public String getEventId() {
        return eventId;
    }
    public void setEventId(String eventId) {
        this.eventId = eventId;
    }
    public String getCategory() {
        return category;
    }
    public void setCategory(String category) {
        this.category = category;
    }
    public EventCategoryResultRequest() {}
    public EventCategoryResultRequest(String eventId, String category, int limit) {
        this.eventId = eventId;
        this.category = category;
    }
    
}