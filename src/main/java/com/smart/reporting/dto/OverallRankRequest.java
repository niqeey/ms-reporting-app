package com.smart.reporting.dto;

public class OverallRankRequest {
    private String eventId;
    private String distance;

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
    public OverallRankRequest() {}
    public OverallRankRequest(String eventId, String distance) {
        this.eventId = eventId;
        this.distance = distance;
    }
}
