package com.compost.monitor.service;

import com.compost.monitor.model.CompostReading;
import com.compost.monitor.repository.ReadingRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.annotation.PostConstruct;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.eclipse.paho.client.mqttv3.*;
import org.eclipse.paho.client.mqttv3.persist.MqttDefaultFilePersistence; // Added this import
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.List;

import com.compost.monitor.model.RetryReading;
import com.compost.monitor.repository.RetryReadingRepository;

@Service
public class MqttService implements MqttCallback {

    private static final Logger log = LoggerFactory.getLogger(MqttService.class);

    @Autowired
    private ReadingRepository readingRepo;

    @Autowired
    private CompostService compostService;

    @Autowired
    private RetryReadingRepository retryReadingRepo;

    private MqttClient client;
    private boolean connected = false;
    private final ObjectMapper objectMapper = new ObjectMapper();

    // persistence-backed retry executor
    private final ScheduledExecutorService retryExecutor = Executors.newSingleThreadScheduledExecutor();

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
            connected = true;

            client.subscribe("compost/sensors");

            log.info(">>> MQTT Connected. Persistence logs moved to: {}", tempDir);

            // start background retry task will be scheduled when application is ready
        } catch (MqttException e) {
            connected = false;
            log.error(">>> MQTT Connection Failed: {}", e.getMessage(), e);
        }
    }

    @EventListener(ApplicationReadyEvent.class)
    public void onApplicationReady() {
        log.info("Starting persisted retry queue drain task after application ready");
        retryExecutor.scheduleAtFixedRate(this::drainRetryQueue, 5, 10, TimeUnit.SECONDS);
    }

    @Override
    public void connectionLost(Throwable cause) {
        connected = false;
        log.warn(">>> MQTT Connection Lost! Attempting auto-reconnect...", cause);
    }

    public boolean isConnected() {
        return connected;
    }

    @Override
    public void messageArrived(String topic, MqttMessage message) {
        try {
            String payload = new String(message.getPayload());
            log.debug(">>> Received MQTT Payload: {}", payload);

            CompostReading reading = objectMapper.readValue(payload, CompostReading.class);
            readingRepo.save(reading);
            compostService.processIntelligence(reading);

            log.debug(">>> Data processed and batch updated successfully.");

        } catch (Exception e) {
            log.error(">>> Error processing message: {}", e.getMessage(), e);
            // persist to retry collection so it can survive restarts
            try {
                String payload = new String(message.getPayload());
                CompostReading reading = objectMapper.readValue(payload, CompostReading.class);
                RetryReading rr = new RetryReading();
                rr.setReading(reading);
                rr.setAttempts(0);
                retryReadingRepo.save(rr);
            } catch (Exception ex) {
                log.error("Failed to parse payload for retry: {}", ex.getMessage(), ex);
            }
        }
    }

    @Override
    public void deliveryComplete(IMqttDeliveryToken token) {
        // Not used for Subscriber
    }

    private void drainRetryQueue() {
        try {
            List<RetryReading> list = retryReadingRepo.findAllByOrderByIdAsc();
            int processed = 0;
            for (RetryReading rr : list) {
                if (processed++ > 50)
                    break; // limit work per run
                try {
                    CompostReading r = rr.getReading();
                    readingRepo.save(r);
                    compostService.processIntelligence(r);
                    retryReadingRepo.deleteById(rr.getId());
                    log.info("Retried and processed persisted reading id {}", r.getId());
                } catch (Exception e) {
                    rr.setAttempts(rr.getAttempts() + 1);
                    // if too many attempts, log and remove to avoid clogging
                    if (rr.getAttempts() > 5) {
                        log.warn("Dropping retry reading {} after {} attempts", rr.getId(), rr.getAttempts());
                        retryReadingRepo.deleteById(rr.getId());
                    } else {
                        retryReadingRepo.save(rr);
                    }
                }
            }
        } catch (Exception e) {
            log.error("Error draining retry queue: {}", e.getMessage(), e);
        }
    }
}