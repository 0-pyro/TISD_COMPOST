package com.compost.monitor.model;

import java.time.LocalDateTime;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Data;

@Data
@Document(collection = "readings")
public class CompostReading {
    @Id
    private String id;
    private Double temperature;
    private Double gasLevel;
    private Double weight;
    private LocalDateTime timestamp = LocalDateTime.now();
}