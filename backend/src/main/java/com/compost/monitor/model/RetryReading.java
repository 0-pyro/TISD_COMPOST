package com.compost.monitor.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "retry_readings")
public class RetryReading {

    @Id
    private String id;

    private CompostReading reading;

    private int attempts = 0;

    public RetryReading() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public CompostReading getReading() {
        return reading;
    }

    public void setReading(CompostReading reading) {
        this.reading = reading;
    }

    public int getAttempts() {
        return attempts;
    }

    public void setAttempts(int attempts) {
        this.attempts = attempts;
    }
}
