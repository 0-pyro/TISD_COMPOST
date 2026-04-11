package com.compost.monitor.repository;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.compost.monitor.model.CompostBatch;

public interface BatchRepository extends MongoRepository<CompostBatch, String> {
    CompostBatch findFirstByStatus(String status);

    CompostBatch findFirstByOrderByStartTimeDesc();
}