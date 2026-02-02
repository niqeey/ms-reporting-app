package com.smart.reporting.dto;

public class LeaderboardRequest {
    private String category;

    public LeaderboardRequest() {
    }

    public LeaderboardRequest(String category) {
        this.category = category;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }
}
