package com.compost.monitor.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.compost.monitor.model.CompostBatch;
import com.compost.monitor.repository.BatchRepository;
import com.compost.monitor.repository.ReadingRepository;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/compost")
public class CompostController {
    @Autowired
    private BatchRepository batchRepo;
    @Autowired
    private ReadingRepository readingRepo;

    @PostMapping("/add-waste")
    public ResponseEntity<?> add(@RequestParam Double weight) {
        CompostBatch activeBatch = batchRepo.findFirstByStatus("ACTIVE");
        if (activeBatch != null) {
            return ResponseEntity.badRequest().body(Map.of("message", "Cannot start a new batch while one is active."));
        }

        CompostBatch batch = new CompostBatch();
        batch.setInitialWeight(weight);
        return ResponseEntity.ok(batchRepo.save(batch));
    }

    @GetMapping("/status")
    public Map<String, Object> getStatus() {
        Map<String, Object> map = new HashMap<>();
        map.put("latestSensor", readingRepo.findFirstByOrderByTimestampDesc());
        map.put("activeBatch", batchRepo.findFirstByStatus("ACTIVE"));
        return map;
    }
}
