package com.cicd.automation.dto;

import java.util.Date;

public class ApiResponse<T> {

    private String message;
    private T data;
    private boolean success;
    private Date timestamp;

    // Constructors
    public ApiResponse() {
        this.timestamp = new Date();
        this.success = true;
    }

    public ApiResponse(String message, T data) {
        this();
        this.message = message;
        this.data = data;
    }

    // Getters and Setters
    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }
}
