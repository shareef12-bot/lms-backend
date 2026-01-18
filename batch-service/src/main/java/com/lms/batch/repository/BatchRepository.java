package com.lms.batch.repository;

import com.lms.batch.entity.Batch;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BatchRepository extends JpaRepository<Batch, Long> {
    List<Batch> findByTrainerId(Long trainerId);
}
