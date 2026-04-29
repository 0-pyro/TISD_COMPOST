package com.compost.monitor.service;

import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.compost.monitor.model.Alert;
import com.compost.monitor.model.CompostBatch;
import com.compost.monitor.model.CompostReading;
import com.compost.monitor.repository.BatchRepository;

@Service
public class CompostService {
    @Autowired
    private BatchRepository batchRepo;

    @Autowired
    private AlertService alertService;

    public void processIntelligence(CompostReading reading) {
        CompostBatch batch = batchRepo.findFirstByStatus("ACTIVE");
        if (batch == null)
            return;

        // 1. Logic for Progress (Example: moves up if temperature is optimal)
        if (reading.getTemperature() > 40.0 && batch.getProgress() < 100) {
            batch.setProgress(batch.getProgress() + 0.5);
        }

        // 2. Logic for Alerts
        if (reading.getGasLevel() > 500) {
            batch.setAlertMessage("ALERT: High Methane detected!");
            alertService.save(createAlert("CRITICAL", "High methane detected", batch.getId()));
        } else if (reading.getTemperature() > 70) {
            batch.setAlertMessage("WARNING: Temperature too high!");
            alertService.save(createAlert("WARNING", "Temperature exceeded safe threshold", batch.getId()));
        } else {
            batch.setAlertMessage("Decomposition stable.");
        }

        // 3. Mark as Ready
        // Inside processIntelligence method:
        if (batch.getProgress() >= 100) {
            batch.setStatus("COMPLETED"); // Match the frontend 'COMPLETED' string

            // Simple logic: 40% of waste becomes compost
            double finalYield = batch.getInitialWeight() * 0.4;
            batch.setCompost(finalYield);
            batch.setAvailable(finalYield);

            batch.setEndTime(LocalDateTime.now());
            batch.setAlertMessage("Compost is ready");
        }

        batchRepo.save(batch);
    }

    private Alert createAlert(String type, String message, String batchId) {
        Alert alert = new Alert();
        alert.setType(type);
        alert.setMessage(message);
        alert.setBatchId(batchId);
        return alert;
    }
}