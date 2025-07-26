package com.smart.reporting.dto;

public class StatisticReportDto {
    private String statistic;
    private String cat;
    private String category;
    private Integer registered;
    private Integer started;
    private Integer didNotStart;
    private Integer finished;
    private Integer didNotFinish;
    private Integer falseStart;
    private Integer noStartButFinished;
    private Integer disqualified;
    private Integer distance;





    public StatisticReportDto() {
        
    }

    public String getStatistic() {
        return statistic;
    }

    public void setStatistic(String statistic) {
        this.statistic = statistic;
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

    public Integer getRegistered() {
        return registered;
    }

    public void setRegistered(Integer registered) {
        this.registered = registered;
    }

    public Integer getStarted() {
        return started;
    }

    public void setStarted(Integer started) {
        this.started = started;
    }

    public Integer getDidNotStart() {
        return didNotStart;
    }

    public void setDidNotStart(Integer didNotStart) {
        this.didNotStart = didNotStart;
    }

    public Integer getFinished() {
        return finished;
    }

    public void setFinished(Integer finished) {
        this.finished = finished;
    }

    public Integer getDidNotFinish() {
        return didNotFinish;
    }

    public void setDidNotFinish(Integer didNotFinish) {
        this.didNotFinish = didNotFinish;
    }

    public Integer getFalseStart() {
        return falseStart;
    }

    public void setFalseStart(Integer falseStart) {
        this.falseStart = falseStart;
    }

    public Integer getNoStartButFinished() {
        return noStartButFinished;
    }

    public void setNoStartButFinished(Integer noStartButFinished) {
        this.noStartButFinished = noStartButFinished;
    }
    public Integer getDistance() {
        return distance;
    }
    public void setDistance(Integer distance) {
        this.distance = distance;
    }
    public Integer getDisqualified() {
        return disqualified;
    }
    public void setDisqualified(Integer disqualified) {
        this.disqualified = disqualified;
    }
}