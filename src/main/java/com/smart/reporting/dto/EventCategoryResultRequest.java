package com.smart.reporting.dto;

public class EventCategoryResultRequest {
    private String eventId;
    private String category;
    private String mode; // "NORMAL" or "LAP"
    private String raceMode; // Alternative name for mode: "NORMAL" or "LAP"

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
    public String getMode() {
        // Support both 'mode' and 'raceMode' field names, with 'raceMode' having priority
        if (raceMode != null) {
            return raceMode;
        }
        return mode != null ? mode : "NORMAL";
    }
    public void setMode(String mode) {
        this.mode = mode;
    }
    public String getRaceMode() {
        return raceMode;
    }
    public void setRaceMode(String raceMode) {
        this.raceMode = raceMode;
    }
    public EventCategoryResultRequest() {}
    public EventCategoryResultRequest(String eventId, String category, int limit) {
        this.eventId = eventId;
        this.category = category;
        this.mode = "NORMAL";
    }
    
}