package com.smart.reporting.dto;

public class StatisticRegListDto {
    private String item;
    private String category;
    private String bib;
    private String name;

    
    public StatisticRegListDto() {
    }
    public StatisticRegListDto(String item, String category, String bib, String name) {
        this.item = item;
        this.category = category;
        this.bib = bib;
        this.name = name;
    }
    public String getItem() {
        return item;
    }
    public void setItem(String item) {
        this.item = item;
    }
    public String getCategory() {
        return category;
    }
    public void setCategory(String category) {
        this.category = category;
    }
    public String getBib() {
        return bib;
    }
    public void setBib(String bib) {
        this.bib = bib;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
}
