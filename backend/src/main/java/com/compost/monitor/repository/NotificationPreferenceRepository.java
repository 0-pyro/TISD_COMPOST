package com.compost.monitor.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import com.compost.monitor.model.NotificationPreference;

public interface NotificationPreferenceRepository extends MongoRepository<NotificationPreference, String> {
}
