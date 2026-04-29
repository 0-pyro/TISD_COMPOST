package com.compost.monitor.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.compost.monitor.model.Alert;
import com.compost.monitor.repository.AlertRepository;
import com.compost.monitor.notification.NotificationManager;
import org.springframework.beans.factory.annotation.Qualifier;

@Service
public class AlertService {

    @Autowired
    private AlertRepository alertRepo;

    @Autowired(required = false)
    private NotificationManager notificationManager;

    public Alert save(Alert alert) {
        Alert saved = alertRepo.save(alert);
        try {
            if (notificationManager != null && saved != null) {
                // notify for important alert types
                String t = saved.getType();
                if (t != null && (t.equalsIgnoreCase("CRITICAL") || t.equalsIgnoreCase("WARNING"))) {
                    notificationManager.notifyAll(saved);
                }
            }
        } catch (Exception e) {
            // swallow to avoid breaking save
        }
        return saved;
    }

    public List<Alert> getActiveAlerts() {
        return alertRepo.findByResolvedFalseOrderByTimestampDesc();
    }

    public Alert resolve(String alertId) {
        Alert alert = alertRepo.findById(alertId).orElse(null);
        if (alert != null) {
            alert.setResolved(true);
            return alertRepo.save(alert);
        }
        return null;
    }
}
