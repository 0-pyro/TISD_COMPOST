package com.compost.monitor.model;

import java.time.LocalDateTime;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Data;

@Data
@Document(collection = "batches")
public class CompostBatch {
    @Id
    private String id;
    private Double initialWeight;
    private Double progress = 0.0;
    private String status = "ACTIVE"; // ACTIVE, READY, COLLECTED
    private String alertMessage = "System Normal";
    private LocalDateTime startTime = LocalDateTime.now();
}