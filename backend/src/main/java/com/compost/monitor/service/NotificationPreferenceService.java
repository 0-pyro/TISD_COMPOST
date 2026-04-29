package com.compost.monitor.service;

import java.util.Optional;

import org.springframework.stereotype.Service;

import com.compost.monitor.model.NotificationPreference;
import com.compost.monitor.repository.NotificationPreferenceRepository;

@Service
public class NotificationPreferenceService {

    private final NotificationPreferenceRepository repo;

    public NotificationPreferenceService(NotificationPreferenceRepository repo) {
        this.repo = repo;
    }

    public Optional<NotificationPreference> getDefault() {
        // For now, return the first preference document if exists
        return repo.findAll().stream().findFirst();
    }

    public NotificationPreference save(NotificationPreference pref) {
        return repo.save(pref);
    }

    public void delete(String id) {
        repo.deleteById(id);
    }
}
