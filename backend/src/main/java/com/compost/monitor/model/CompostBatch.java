package com.compost.monitor.model;

import java.time.LocalDateTime;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "batches")
public class CompostBatch {
    @Id
    private String id;
    private Double initialWeight;
    private Double progress = 0.0;
    private String status = "ACTIVE"; // ACTIVE, COMPLETED
    private String alertMessage = "System Normal";
    private LocalDateTime startTime = LocalDateTime.now();
    private LocalDateTime endTime;
    private Double compost = 0.0;
    private Double available = 0.0;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Double getInitialWeight() {
        return initialWeight;
    }

    public void setInitialWeight(Double initialWeight) {
        this.initialWeight = initialWeight;
    }

    public Double getProgress() {
        return progress;
    }

    public void setProgress(Double progress) {
        this.progress = progress;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getAlertMessage() {
        return alertMessage;
    }

    public void setAlertMessage(String alertMessage) {
        this.alertMessage = alertMessage;
    }

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalDateTime startTime) {
        this.startTime = startTime;
    }

    public LocalDateTime getEndTime() {
        return endTime;
    }

    public void setEndTime(LocalDateTime endTime) {
        this.endTime = endTime;
    }

    public Double getCompost() {
        return compost;
    }

    public void setCompost(Double compost) {
        this.compost = compost;
    }

    public Double getAvailable() {
        return available;
    }

    public void setAvailable(Double available) {
        this.available = available;
    }
}
