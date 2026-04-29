package com.compost.monitor.repository;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.compost.monitor.model.Alert;

public interface AlertRepository extends MongoRepository<Alert, String> {
    List<Alert> findByResolvedFalseOrderByTimestampDesc();
}
