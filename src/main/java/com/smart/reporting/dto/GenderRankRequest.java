package com.smart.reporting.dto;

public class GenderRankRequest {
    private String eventId;
    private String distance;
    private String gender;

    public String getEventId() {
        return eventId;
    }
    public void setEventId(String eventId) {
        this.eventId = eventId;
    }
    public String getDistance() {
        return distance;
    }
    public void setDistance(String distance) {
        this.distance = distance;
    }
    public String getGender() {
        return gender;
    }
    public void setGender(String gender) {
        this.gender = gender;
    }
    public GenderRankRequest() {}
    public GenderRankRequest(String eventId, String distance, String gender) {
        this.eventId = eventId;
        this.distance = distance;
        this.gender = gender;
    }
}
