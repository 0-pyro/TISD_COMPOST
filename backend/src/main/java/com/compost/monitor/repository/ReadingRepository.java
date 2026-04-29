package com.compost.monitor.repository;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.compost.monitor.model.CompostReading;

public interface ReadingRepository extends MongoRepository<CompostReading, String> {
    CompostReading findFirstByOrderByTimestampDesc();

    List<CompostReading> findByTimestampBetweenOrderByTimestampDesc(LocalDateTime start, LocalDateTime end);
}