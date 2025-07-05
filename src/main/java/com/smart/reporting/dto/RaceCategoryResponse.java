package com.smart.reporting.dto;

import java.math.BigDecimal;

public class RaceCategoryResponse {
    private Integer catId;
    private String eventId;
    private String cat;
    private String category;//name
    private BigDecimal distance;
    private String race;
    private String gender;
    private Integer timegun;
    private Integer timegun1;
    private Integer timegun2;
    private Integer timegun3;
    private Integer timegun4;
    private Integer timegun5;
    private Integer timegun6;
    private String checkpointlist;
    private String raceMode;
    private int toplist;
    private int islive;

    

    public Integer getCatId() {
        return catId;
    }

    public void setCatId(Integer catId) {
        this.catId = catId;
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

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public BigDecimal getDistance() {
        return distance;
    }

    public void setDistance(BigDecimal distance) {
        this.distance = distance;
    }

    public String getRace() {
        return race;
    }

    public void setRace(String race) {
        this.race = race;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public Integer getTimegun() {
        return timegun;
    }

    public void setTimegun(Integer guntime) {
        this.timegun = guntime;
    }

    public Integer getTimegun1() {
        return timegun1;
    }

    public void setTimegun1(Integer guntime1) {
        this.timegun1 = guntime1;
    }

    public Integer getTimegun2() {
        return timegun2;
    }

    public void setTimegun2(Integer guntime2) {
        this.timegun2 = guntime2;
    }

    public Integer getTimegun3() {
        return timegun3;
    }

    public void setTimegun3(Integer guntime3) {
        this.timegun3 = guntime3;
    }

    public Integer getTimegun4() {
        return timegun4;
    }

    public void setTimegun4(Integer guntime4) {
        this.timegun4 = guntime4;
    }

    public Integer getTimegun5() {
        return timegun5;
    }

    public void setTimegun5(Integer guntime5) {
        this.timegun5 = guntime5;
    }

    public Integer getTimegun6() {
        return timegun6;
    }

    public void setTimegun6(Integer guntime6) {
        this.timegun6 = guntime6;
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

    public int getIslive() {
        return islive;
    }

    public void setIslive(int islive) {
        this.islive = islive;
    }
}