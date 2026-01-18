package com.lms.student.repo;

import com.lms.student.model.Trainer;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TrainerRepository extends JpaRepository<Trainer, Long> {

    boolean existsByEmail(String email);
}
