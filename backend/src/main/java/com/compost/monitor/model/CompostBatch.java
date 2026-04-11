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
    private String status = "ACTIVE"; // ACTIVE, READY, COMPLETED, COLLECTED
    private String alertMessage = "System Normal";
    private LocalDateTime startTime = LocalDateTime.now();
    private LocalDateTime endTime; // New: To track when it finished

    // New: Fields for the Farmer Request logic
    private Double compost = 0.0; // The final yield (e.g., 50% of initial weight)
    private Double available = 0.0; // What's left after farmers take some
}