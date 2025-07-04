package com.smart.reporting.dto;

public class EventRequest {
    private String eventId;

    public String getEventId() {
        return eventId;
    }
    public void setEventId(String eventId) {
        this.eventId = eventId;
    }
   
    public EventRequest() {}
    public EventRequest(String eventId) {
        this.eventId = eventId;
    }
    
}
