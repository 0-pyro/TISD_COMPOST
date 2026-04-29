package com.compost.monitor.notification;

import com.compost.monitor.model.Alert;

public interface NotificationService {
    void send(Alert alert);

    String getName();
}
