package com.compost.monitor.controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.compost.monitor.model.CompostBatch;
import com.compost.monitor.repository.BatchRepository;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/batches")
public class BatchController {
    @Autowired
    private BatchRepository batchRepo;

    // For RequestForm.jsx: gets the most recent batch
    @GetMapping("/latest")
    public CompostBatch getLatest() {
        return batchRepo.findFirstByOrderByStartTimeDesc();
    }

    // For BatchHistory.jsx: gets all past batches
    @GetMapping("/history")
    public List<CompostBatch> getHistory() {
        return batchRepo.findAll(Sort.by(Sort.Direction.DESC, "startTime"));
    }

    // For delivery stats
    @GetMapping("/stats/delivery")
    public Map<String, Object> getDeliveryStats() {
        List<CompostBatch> batches = batchRepo.findAll();
        double totalDelivered = batches.stream().mapToDouble(b -> b.getCompost() - b.getAvailable()).sum();
        return Map.of("totalDelivered", totalDelivered);
    }

    // The logic for the Farmer Request
    @PostMapping("/request")
    public ResponseEntity<?> handleRequest(@RequestBody Map<String, Object> payload) {
        String batchId = (String) payload.get("batchId");
        Double amount = Double.parseDouble(payload.get("amount").toString());

        CompostBatch batch = batchRepo.findById(batchId).orElse(null);
        if (batch == null)
            return ResponseEntity.badRequest().body("Batch not found");

        if (batch.getAvailable() < amount) {
            return ResponseEntity.badRequest().body(Map.of("message", "Not enough compost available!"));
        }

        batch.setAvailable(batch.getAvailable() - amount);
        batchRepo.save(batch);
        return ResponseEntity.ok(Map.of("message", "Success"));
    }
}
