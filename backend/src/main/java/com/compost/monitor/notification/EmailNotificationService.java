package com.compost.monitor.notification;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import com.compost.monitor.model.Alert;

@Service
public class EmailNotificationService implements NotificationService {

    private static final Logger log = LoggerFactory.getLogger(EmailNotificationService.class);

    private final JavaMailSender mailSender;

    @Value("${app.notifications.email.from:compost@example.com}")
    private String from;

    @Value("${app.notifications.email.to:admin@example.com}")
    private String defaultTo;

    public EmailNotificationService(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    @Override
    public void send(Alert alert) {
        try {
            SimpleMailMessage msg = new SimpleMailMessage();
            msg.setFrom(from);
            msg.setTo(defaultTo);
            msg.setSubject("Compost Alert: " + alert.getType());
            msg.setText(alert.getMessage() + "\nBatch: " + alert.getBatchId() + "\nTime: " + alert.getTimestamp());
            mailSender.send(msg);
            log.info("Email notification sent for alert {}", alert.getId());
        } catch (Exception e) {
            log.error("Failed to send email notification for alert {}", alert.getId(), e);
        }
    }

    @Override
    public String getName() {
        return "email";
    }
}
