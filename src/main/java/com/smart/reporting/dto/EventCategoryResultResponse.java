package com.smart.reporting.dto;

import java.sql.Time;

public class EventCategoryResultResponse {
    // Add fields you want to expose in the response
    private String cplist;
    private String name;
    private String bib;
    private String category;
    private String eventId;
    private String eventName;
    private String cat;
    private int rank1Cat;
    private int rank1Mix;
    private int rank1Tot;
    private String timeStart;
    private String timeFinish;
    private String timeGun;
    private String timeCP1;
    private String timeCP2;
    private String timeCP3;
    private String timeCP4;
    private String timeCP5;
    private String timeCP6;
    private String timeCP7;
    private String timeCP8;
    private String timeCP9;
    private String timeCP10;
    private String netTime;
    private String officialTime;
    
    
     // Constructor
    public EventCategoryResultResponse() {}
    public EventCategoryResultResponse(String cplist,String name, String bib, String category, String eventId, String eventName, String cat, int rank1cat, int rank1mix, int rank1tot, String timeStart, String timeFinish,
            String timeGun, String timeCP1, String timeCP2, String timeCP3, String timeCP4, String timeCP5, String timeCP6, String timeCP7,
            String timeCP8, String timeCP9, String timeCP10, String netTime, String officialTime) {
        this.cplist = cplist;
        this.name = name;
        this.bib = bib;
        this.category = category;
        this.eventId = eventId;
        this.eventName = eventName;
        this.cat = cat;
        this.rank1Cat = rank1cat;
        this.rank1Mix = rank1mix;
        this.rank1Tot = rank1tot;
        this.timeStart = timeStart;
        this.timeFinish = timeFinish;
        this.timeGun = timeGun;
        this.timeCP1 = timeCP1;
        this.timeCP2 = timeCP2;
        this.timeCP3 = timeCP3;
        this.timeCP4 = timeCP4;
        this.timeCP5 = timeCP5;
        this.timeCP6 = timeCP6;
        this.timeCP7 = timeCP7;
        this.timeCP8 = timeCP8;
        this.timeCP9 = timeCP9;
        this.timeCP10 = timeCP10;
        this.netTime = netTime;
        this.officialTime = officialTime;
    }
    public String getCplist() {
        return cplist;
    }
    public void setCplist(String cplist) {
        this.cplist = cplist;
    }
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
    public int getRank1Cat() {
        return rank1Cat;
    }
    public void setRank1Cat(int rank1Cat) {
        this.rank1Cat = rank1Cat;
    }
    public int getRank1Mix() {
        return rank1Mix;
    }
    public void setRank1Mix(int rank1Mix) {
        this.rank1Mix = rank1Mix;
    }

    public int getRank1Tot() {
        return rank1Tot;
    }
    public void setRank1Tot(int rank1Tot) {
        this.rank1Tot = rank1Tot;
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
    public String getTimeCP1() {
        return timeCP1;
    }
    public void setTimeCP1(String timeCP1) {
        this.timeCP1 = timeCP1;
    }
    public String getTimeCP2() {
        return timeCP2;
    }
    public void setTimeCP2(String timeCP2) {
        this.timeCP2 = timeCP2;
    }
    public String getTimeCP3() {
        return timeCP3;
    }
    public void setTimeCP3(String timeCP3) {
        this.timeCP3 = timeCP3;
    }
    public String getTimeCP4() {
        return timeCP4;
    }
    public void setTimeCP4(String timeCP4) {
        this.timeCP4 = timeCP4;
    }
    public String getTimeCP5() {
        return timeCP5;
    }
    public void setTimeCP5(String timeCP5) {
        this.timeCP5 = timeCP5;
    }
    public String getTimeCP6() {
        return timeCP6;
    }
    public void setTimeCP6(String timeCP6) {
        this.timeCP6 = timeCP6;
    }
    public String getTimeCP7() {
        return timeCP7;
    }
    public void setTimeCP7(String timeCP7) {
        this.timeCP7 = timeCP7;
    }
    public String getTimeCP8() {
        return timeCP8;
    }
    public void setTimeCP8(String timeCP8) {
        this.timeCP8 = timeCP8;
    }
    public String getTimeCP9() {
        return timeCP9;
    }
    public void setTimeCP9(String timeCP9) {
        this.timeCP9 = timeCP9;
    }
    public String getTimeCP10() {
        return timeCP10;
    }
    public void setTimeCP10(String timeCP10) {
        this.timeCP10 = timeCP10;
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

}