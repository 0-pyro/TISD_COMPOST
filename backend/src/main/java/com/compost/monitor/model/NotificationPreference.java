package com.compost.monitor.model;

import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "notification_preferences")
public class NotificationPreference {

    @Id
    private String id;

    // recipient email (default)
    private String email;

    // enabled channels, e.g. ["email","webhook","sms"]
    private List<String> channels;

    // minimum severity to notify: INFO, WARNING, CRITICAL
    private String minSeverity;

    public NotificationPreference() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public List<String> getChannels() {
        return channels;
    }

    public void setChannels(List<String> channels) {
        this.channels = channels;
    }

    public String getMinSeverity() {
        return minSeverity;
    }

    public void setMinSeverity(String minSeverity) {
        this.minSeverity = minSeverity;
    }
}
