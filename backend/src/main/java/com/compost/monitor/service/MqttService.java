package com.compost.monitor.service;

import com.compost.monitor.model.CompostReading;
import com.compost.monitor.repository.ReadingRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.annotation.PostConstruct;
import org.eclipse.paho.client.mqttv3.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MqttService implements MqttCallback {

    @Autowired
    private ReadingRepository readingRepo;

    @Autowired
    private CompostService compostService;

    private MqttClient client;
    private final ObjectMapper objectMapper = new ObjectMapper();

    @PostConstruct
    public void init() {
        try {
            // Broker Address (localhost for now, will change for ESP32)
            String broker = "tcp://localhost:1883";
            String clientId = MqttClient.generateClientId();

            client = new MqttClient(broker, clientId);

            MqttConnectOptions options = new MqttConnectOptions();
            options.setCleanSession(true);
            options.setAutomaticReconnect(true);
            options.setConnectionTimeout(10);

            client.setCallback(this);
            client.connect(options);

            // Topic the ESP32 will publish to
            client.subscribe("compost/sensors");

            System.out.println(">>> MQTT Connected to: " + broker);
        } catch (MqttException e) {
            System.err.println(">>> MQTT Connection Failed: " + e.getMessage());
        }
    }

    @Override
    public void connectionLost(Throwable cause) {
        System.err.println(">>> MQTT Connection Lost! Attempting auto-reconnect...");
    }

    @Override
    public void messageArrived(String topic, MqttMessage message) {
        try {
            String payload = new String(message.getPayload());
            System.out.println(">>> Received MQTT Payload: " + payload);

            // 1. Convert JSON string to Java Object
            CompostReading reading = objectMapper.readValue(payload, CompostReading.class);

            // 2. Save the raw sensor data to MongoDB
            readingRepo.save(reading);

            // 3. Pass data to the brain to update batch progress/alerts
            compostService.processIntelligence(reading);

            System.out.println(">>> Data processed and batch updated successfully.");

        } catch (Exception e) {
            System.err.println(">>> Error processing message: " + e.getMessage());
            // We catch the error so the MQTT thread doesn't die
        }
    }

    @Override
    public void deliveryComplete(IMqttDeliveryToken token) {
        // Not used for Subscriber
    }
}
