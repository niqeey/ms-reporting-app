package com.smart.reporting.dto;

public class StartRaceRequest {
    private String eventId;
    private String cat;
    private Integer halflap;

    public StartRaceRequest() {}

    public StartRaceRequest(String eventId, String cat, Integer halflap) {
        this.eventId = eventId;
        this.cat = cat;
        this.halflap = halflap;
    }

    public String getEventId() {
        return eventId;
    }

    public void setEventId(String eventId) {
        this.eventId = eventId;
    }

    public String getCat() {
        return cat;
    }

    public void setCat(String cat) {
        this.cat = cat;
    }

    public Integer getHalflap() {
        return halflap;
    }

    public void setHalflap(Integer halflap) {
        this.halflap = halflap;
    }
}
