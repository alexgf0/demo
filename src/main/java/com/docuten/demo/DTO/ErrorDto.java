package com.docuten.demo.DTO;

import org.springframework.http.HttpStatus;

import java.util.Date;

public class ErrorDto {
    private int status;
    private String error;
    private String message;
    private String timestamp;

    public ErrorDto(HttpStatus status, String message) {
        this.status = status.value();
        this.error = status.getReasonPhrase();
        this.timestamp = String.valueOf(new Date());
        this.message = message;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }
}
