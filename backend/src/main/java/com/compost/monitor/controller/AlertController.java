package com.compost.monitor.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.compost.monitor.model.Alert;
import com.compost.monitor.service.AlertService;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/alerts")
public class AlertController {

    @Autowired
    private AlertService alertService;

    @GetMapping
    public List<Alert> getActiveAlerts() {
        return alertService.getActiveAlerts();
    }

    @PostMapping("/{id}/resolve")
    public ResponseEntity<?> resolveAlert(@PathVariable String id) {
        Alert alert = alertService.resolve(id);
        if (alert == null) {
            return ResponseEntity.badRequest().body("Alert not found");
        }
        return ResponseEntity.ok(alert);
    }
}
