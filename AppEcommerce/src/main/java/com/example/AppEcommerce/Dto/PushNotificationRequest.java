package com.example.AppEcommerce.Dto;

public class PushNotificationRequest {
    private String deviceToken;
    private String title;
    private String body;

    // Constructors, getters, and setters

    public PushNotificationRequest() {
    }

    public PushNotificationRequest(String deviceToken, String title, String body) {
        this.deviceToken = deviceToken;
        this.title = title;
        this.body = body;
    }

    public String getDeviceToken() {
        return deviceToken;
    }

    public void setDeviceToken(String deviceToken) {
        this.deviceToken = deviceToken;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }
}

