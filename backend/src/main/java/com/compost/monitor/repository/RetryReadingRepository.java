package com.compost.monitor.repository;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;
import com.compost.monitor.model.RetryReading;

public interface RetryReadingRepository extends MongoRepository<RetryReading, String> {
    List<RetryReading> findAllByOrderByIdAsc();
}
