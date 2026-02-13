package com.smart.reporting.dto;

import java.util.HashMap;
import java.util.Map;

public class LapResultResponse {
    // Fields for LAP mode results
    private String name;
    private String bib;
    private String category;
    private String eventId;
    private String eventName;
    private String cat;
    private Integer rank1Cat;
    private Integer lap;
    private Integer bonusLap;
    private Short dqLap;
    private String timeStart;
    private String timeFinish;
    private String timeGun;
    private String lapTime;
    private String netTime;
    private String officialTime;
    private String cplist;
    
    // Simplified: Use a map to store lap times dynamically
    private Map<String, String> lapTimes = new HashMap<>();
    
    public LapResultResponse() {}

    public LapResultResponse(String name, String bib, String category, String eventId, String eventName, String cat,
                           Integer lap, Integer bonusLap, Short dqLap, String timeStart, String timeFinish,
                           String timeGun, String lapTime, String netTime, String officialTime) {
        this.name = name;
        this.bib = bib;
        this.category = category;
        this.eventId = eventId;
        this.eventName = eventName;
        this.cat = cat;
        this.lap = lap;
        this.bonusLap = bonusLap;
        this.dqLap = dqLap;
        this.timeStart = timeStart;
        this.timeFinish = timeFinish;
        this.timeGun = timeGun;
        this.lapTime = lapTime;
        this.netTime = netTime;
        this.officialTime = officialTime;
    }

    // Getters and Setters
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getBib() {
        return bib;
    }

    public void setBib(String bib) {
        this.bib = bib;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
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

    public String getCat() {
        return cat;
    }

    public void setCat(String cat) {
        this.cat = cat;
    }

    public Integer getRank1Cat() {
        return rank1Cat;
    }

    public void setRank1Cat(Integer rank1Cat) {
        this.rank1Cat = rank1Cat;
    }

    public Integer getLap() {
        return lap;
    }

    public void setLap(Integer lap) {
        this.lap = lap;
    }

    public Integer getBonusLap() {
        return bonusLap;
    }

    public void setBonusLap(Integer bonusLap) {
        this.bonusLap = bonusLap;
    }

    public Short getDqLap() {
        return dqLap;
    }

    public void setDqLap(Short dqLap) {
        this.dqLap = dqLap;
    }

    public String getTimeStart() {
        return timeStart;
    }

    public void setTimeStart(String timeStart) {
        this.timeStart = timeStart;
    }

    public String getTimeFinish() {
        return timeFinish;
    }

    public void setTimeFinish(String timeFinish) {
        this.timeFinish = timeFinish;
    }

    public String getTimeGun() {
        return timeGun;
    }

    public void setTimeGun(String timeGun) {
        this.timeGun = timeGun;
    }

    public String getLapTime() {
        return lapTime;
    }

    public void setLapTime(String lapTime) {
        this.lapTime = lapTime;
    }

    public String getNetTime() {
        return netTime;
    }

    public void setNetTime(String netTime) {
        this.netTime = netTime;
    }

    public String getOfficialTime() {
        return officialTime;
    }

    public void setOfficialTime(String officialTime) {
        this.officialTime = officialTime;
    }

    public String getCplist() {
        return cplist;
    }

    public void setCplist(String cplist) {
        this.cplist = cplist;
    }

    // Simplified getter/setter for lap times
    public Map<String, String> getLapTimes() {
        return lapTimes;
    }

    public void setLapTimes(Map<String, String> lapTimes) {
        this.lapTimes = lapTimes;
    }

    // Helper methods to maintain backward compatibility
    public void setLapTime(int lapNumber, String time) {
        if (time != null) {
            lapTimes.put("time" + lapNumber, time);
        }
    }

    public String getLapTime(int lapNumber) {
        return lapTimes.get("time" + lapNumber);
    }
}
