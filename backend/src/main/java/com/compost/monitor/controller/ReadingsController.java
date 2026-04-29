package com.compost.monitor.controller;

import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.compost.monitor.model.CompostReading;
import com.compost.monitor.repository.ReadingRepository;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/readings")
public class ReadingsController {

    @Autowired
    private ReadingRepository readingRepo;

    @GetMapping("/latest")
    public List<CompostReading> getLatestReadings(@RequestParam(defaultValue = "50") int count) {
        if (count <= 0) {
            count = 50;
        }
        return readingRepo.findAll().stream()
                .sorted((a, b) -> b.getTimestamp().compareTo(a.getTimestamp()))
                .limit(count)
                .toList();
    }

    @GetMapping
    public ResponseEntity<?> getReadingsInRange(
            @RequestParam String start,
            @RequestParam String end,
            @RequestParam(defaultValue = "100") int limit) {
        try {
            LocalDateTime startTime = LocalDateTime.parse(start);
            LocalDateTime endTime = LocalDateTime.parse(end);
            if (limit <= 0) {
                limit = 100;
            }
            List<CompostReading> readings = readingRepo.findByTimestampBetweenOrderByTimestampDesc(startTime, endTime);
            return ResponseEntity.ok(readings.stream().limit(limit).toList());
        } catch (DateTimeParseException e) {
            return ResponseEntity.badRequest().body(Map.of("message", "Invalid date format. Use ISO_LOCAL_DATE_TIME."));
        }
    }
}
