package com.compost.monitor.repository;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.compost.monitor.model.CompostReading;

public interface ReadingRepository extends MongoRepository<CompostReading, String> {
    CompostReading findFirstByOrderByTimestampDesc();
}