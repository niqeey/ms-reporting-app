package com.smart.reporting.dto;

import java.math.BigDecimal;

public class EventCategoryResponse {
    private String eventId;
    private String eventName;
    private Long catId;
    private String cat;
    private String name;
    private BigDecimal distance;
    private String gender;
    private String checkpointlist;
    private String raceMode;
    private int toplist;
    private int topprize;
    private int islive;
    private Boolean archived;


    public BigDecimal getDistance() {
        return distance;
    }

    public void setDistance(BigDecimal distance) {
        this.distance = distance;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getCheckpointlist() {
        return checkpointlist;
    }

    public void setCheckpointlist(String checkpointlist) {
        this.checkpointlist = checkpointlist;
    }

    public String getRaceMode() {
        return raceMode;
    }

    public void setRaceMode(String raceMode) {
        this.raceMode = raceMode;
    }

    public int getToplist() {
        return toplist;
    }

    public void setToplist(int toplist) {
        this.toplist = toplist;
    }

    public int getTopprize() {
        return topprize;
    }

    public void setTopprize(int topprize) {
        this.topprize = topprize;
    }

    public int getIslive() {
        return islive;
    }

    public void setIslive(int islive) {
        this.islive = islive;
    }
    private int isresult;


    public int getIsresult() {
        return isresult;
    }

    public void setIsresult(int isresult) {
        this.isresult = isresult;
    }

    public Boolean getArchived() {
        return archived;
    }

    public void setArchived(Boolean archived) {
        this.archived = archived;
    }

    public EventCategoryResponse() {}

    public EventCategoryResponse(
        String eventId, 
        String eventName, 
        Long catId, 
        String cat, 
        String name,
        BigDecimal distance,
        String gender,
        String checkpointlist,
        String raceMode,
        int toplist,
        int topprize,
        int islive,
        int isresult,
        Boolean archived
    ) {
        this.eventId = eventId;
        this.eventName = eventName;
        this.catId = catId;
        this.cat = cat;
        this.name = name;
        this.distance = distance;
        this.gender = gender;
        this.checkpointlist = checkpointlist;
        this.raceMode = raceMode;
        this.toplist = toplist;
        this.topprize = topprize;
        this.islive = islive;
        this.isresult = isresult;
        this.archived = archived;
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
