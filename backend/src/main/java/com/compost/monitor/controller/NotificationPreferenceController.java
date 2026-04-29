package com.compost.monitor.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.compost.monitor.model.NotificationPreference;
import com.compost.monitor.repository.NotificationPreferenceRepository;

@RestController
@RequestMapping("/api/notification-preferences")
public class NotificationPreferenceController {

    private final NotificationPreferenceRepository repo;

    public NotificationPreferenceController(NotificationPreferenceRepository repo) {
        this.repo = repo;
    }

    @GetMapping
    public List<NotificationPreference> list() {
        return repo.findAll();
    }

    @PostMapping
    public NotificationPreference create(@RequestBody NotificationPreference pref) {
        return repo.save(pref);
    }

    @PutMapping("/{id}")
    public ResponseEntity<NotificationPreference> update(@PathVariable String id,
            @RequestBody NotificationPreference pref) {
        pref.setId(id);
        return ResponseEntity.ok(repo.save(pref));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable String id) {
        repo.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
