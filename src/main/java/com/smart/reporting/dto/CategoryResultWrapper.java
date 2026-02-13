package com.smart.reporting.dto;

import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Wrapper class to support both EventCategoryResultResponse (NORMAL mode)
 * and LapResultResponse (LAP mode) in a type-safe manner
 */
public class CategoryResultWrapper {
    private String mode; // "NORMAL" or "LAP"
    private Object data; // Can be EventCategoryResultResponse or LapResultResponse

    public CategoryResultWrapper() {}

    public CategoryResultWrapper(String mode, Object data) {
        this.mode = mode;
        this.data = data;
    }

    public String getMode() {
        return mode;
    }

    public void setMode(String mode) {
        this.mode = mode;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }
}
