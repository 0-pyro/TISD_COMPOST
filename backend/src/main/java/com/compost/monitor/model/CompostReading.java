package com.compost.monitor.model;

import java.time.LocalDateTime;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "readings")
public class CompostReading {
    @Id
    private String id;
    private Double temperature;
    private Double gasLevel;
    private Double weight;
    private LocalDateTime timestamp = LocalDateTime.now();

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Double getTemperature() {
        return temperature;
    }

    public void setTemperature(Double temperature) {
        this.temperature = temperature;
    }

    public Double getGasLevel() {
        return gasLevel;
    }

    public void setGasLevel(Double gasLevel) {
        this.gasLevel = gasLevel;
    }

    public Double getWeight() {
        return weight;
    }

    public void setWeight(Double weight) {
        this.weight = weight;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }
}
