package com.compost.monitor.service;

import com.compost.monitor.model.CompostReading;
import com.compost.monitor.repository.ReadingRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.annotation.PostConstruct;
import org.eclipse.paho.client.mqttv3.*;
import org.eclipse.paho.client.mqttv3.persist.MqttDefaultFilePersistence; // Added this import
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
            String broker = "tcp://localhost:1883";
            String clientId = MqttClient.generateClientId();

            // 1. Define the temporary directory for persistence files
            String tempDir = System.getProperty("java.io.tmpdir");
            MqttDefaultFilePersistence persistence = new MqttDefaultFilePersistence(tempDir);

            // 2. Initialize the client with persistence
            client = new MqttClient(broker, clientId, persistence);

            MqttConnectOptions options = new MqttConnectOptions();
            options.setCleanSession(true);
            options.setAutomaticReconnect(true);
            options.setConnectionTimeout(10);

            client.setCallback(this);
            client.connect(options);

            client.subscribe("compost/sensors");

            System.out.println(">>> MQTT Connected. Persistence logs moved to: " + tempDir);
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

            CompostReading reading = objectMapper.readValue(payload, CompostReading.class);
            readingRepo.save(reading);
            compostService.processIntelligence(reading);

            System.out.println(">>> Data processed and batch updated successfully.");

        } catch (Exception e) {
            System.err.println(">>> Error processing message: " + e.getMessage());
        }
    }

    @Override
    public void deliveryComplete(IMqttDeliveryToken token) {
        // Not used for Subscriber
    }
}