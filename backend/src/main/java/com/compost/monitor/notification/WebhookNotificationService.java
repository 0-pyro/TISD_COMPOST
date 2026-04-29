package com.compost.monitor.notification;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.compost.monitor.model.Alert;

@Service
public class WebhookNotificationService implements NotificationService {

    private static final Logger log = LoggerFactory.getLogger(WebhookNotificationService.class);

    private final RestTemplate rest = new RestTemplate();

    @Value("${app.notifications.webhook.url:}")
    private String webhookUrl;

    @Override
    public void send(Alert alert) {
        if (webhookUrl == null || webhookUrl.isBlank()) {
            return;
        }
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            String payload = String.format("{\"id\":\"%s\",\"type\":\"%s\",\"message\":\"%s\",\"batchId\":\"%s\"}",
                    alert.getId(), alert.getType(), alert.getMessage(), alert.getBatchId());
            HttpEntity<String> entity = new HttpEntity<>(payload, headers);
            rest.postForEntity(webhookUrl, entity, String.class);
            log.info("Webhook notification posted for alert {}", alert.getId());
        } catch (Exception e) {
            log.error("Failed to post webhook for alert {}", alert.getId(), e);
        }
    }

    @Override
    public String getName() {
        return "webhook";
    }
}
