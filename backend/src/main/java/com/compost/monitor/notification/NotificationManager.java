package com.compost.monitor.notification;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.compost.monitor.model.Alert;
import com.compost.monitor.model.NotificationPreference;
import com.compost.monitor.service.NotificationPreferenceService;
import java.util.Set;

@Component
public class NotificationManager {

    private static final Logger log = LoggerFactory.getLogger(NotificationManager.class);

    private final List<NotificationService> services;
    private final NotificationPreferenceService preferenceService;

    public NotificationManager(List<NotificationService> services, NotificationPreferenceService preferenceService) {
        this.services = services;
        this.preferenceService = preferenceService;
    }

    public void notifyAll(Alert alert) {
        // consult preferences (default/global) to determine enabled channels
        try {
            NotificationPreference pref = preferenceService.getDefault().orElse(null);
            Set<String> enabled = pref != null && pref.getChannels() != null ? Set.copyOf(pref.getChannels()) : null;
            for (NotificationService s : services) {
                try {
                    if (enabled != null && !enabled.contains(s.getName())) {
                        continue;
                    }
                    s.send(alert);
                } catch (Throwable t) {
                    log.error("Notification service {} failed for alert {}", s.getName(), alert.getId(), t);
                }
            }
        } catch (Exception e) {
            log.warn("Failed to load notification preferences, falling back to notify-all", e);
            for (NotificationService s : services) {
                try {
                    s.send(alert);
                } catch (Throwable t) {
                    log.error("{} failed", s.getName(), t);
                }
            }
        }
    }
}
