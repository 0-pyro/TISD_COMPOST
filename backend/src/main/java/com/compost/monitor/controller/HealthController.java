package com.compost.monitor.controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.compost.monitor.repository.ReadingRepository;
import com.compost.monitor.service.MqttService;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/health")
public class HealthController {

    @Autowired
    private MqttService mqttService;

    @Autowired
    private ReadingRepository readingRepository;

    @GetMapping
    public Map<String, Object> getHealth() {
        String dbStatus = "DISCONNECTED";
        try {
            readingRepository.count();
            dbStatus = "CONNECTED";
        } catch (Exception ignored) {
        }
        return Map.of(
                "status", "UP",
                "db", dbStatus,
                "mqtt", mqttService.isConnected() ? "CONNECTED" : "DISCONNECTED");
    }
}
