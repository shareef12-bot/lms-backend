package com.lms.enrollment.repository;

import com.lms.enrollment.model.Enrollment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface EnrollmentRepository extends JpaRepository<Enrollment, Long> {

    List<Enrollment> findByUserId(Long userId);

    List<Enrollment> findByCourseId(Long courseId);
}
