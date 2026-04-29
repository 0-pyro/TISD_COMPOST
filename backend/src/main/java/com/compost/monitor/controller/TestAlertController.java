package com.compost.monitor.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.compost.monitor.model.Alert;
import com.compost.monitor.service.AlertService;

@RestController
@RequestMapping("/api/test-alerts")
public class TestAlertController {

    private final AlertService alertService;

    public TestAlertController(AlertService alertService) {
        this.alertService = alertService;
    }

    @PostMapping
    public ResponseEntity<Alert> sendTestAlert(@RequestBody Alert alert) {
        // allow creating an alert directly to test notifications
        Alert saved = alertService.save(alert);
        return ResponseEntity.ok(saved);
    }
}
