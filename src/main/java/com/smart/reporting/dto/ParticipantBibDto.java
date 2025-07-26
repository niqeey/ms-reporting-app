package com.smart.reporting.dto;

public class ParticipantBibDto {
    private String eventId;
    private String bib;

    public ParticipantBibDto(String eventId, String bib) {
        this.bib = bib;
        this.eventId = eventId;
    }

    public String getBib() {
        return bib;
    }

    public void setBib(String bib) {
        this.bib = bib;
    }
    public String getEventId() {
        return eventId;
    }
    public void setEventId(String eventId) {
        this.eventId = eventId;
    }
}
