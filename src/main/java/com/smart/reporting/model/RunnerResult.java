package com.smart.reporting.model;

public class RunnerResult {
    private int rank;
    private String name;
    private String time;

    public RunnerResult(int rank, String name, String time) {
        this.rank = rank;
        this.name = name;
        this.time = time;
    }

    public int getRank() { return rank; }
    public String getName() { return name; }
    public String getTime() { return time; }
}
