package com.lms.batch.repository;

import com.lms.batch.entity.BatchStudent;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BatchStudentRepository extends JpaRepository<BatchStudent, Long> {
    long countByBatchId(Long batchId);
}
