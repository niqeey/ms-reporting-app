package com.smart.reporting.dto;

import java.util.List;

/**
 * Wrapper class for category result lists that supports both NORMAL and LAP modes
 */
public class CategoryResultListWrapper {
    private String mode; // "NORMAL" or "LAP"
    private List<?> data; // Can be List<EventCategoryResultResponse> or List<LapResultResponse>

    public CategoryResultListWrapper() {}

    public CategoryResultListWrapper(String mode, List<?> data) {
        this.mode = mode;
        this.data = data;
    }

    public String getMode() {
        return mode;
    }

    public void setMode(String mode) {
        this.mode = mode;
    }

    public List<?> getData() {
        return data;
    }

    public void setData(List<?> data) {
        this.data = data;
    }
}
