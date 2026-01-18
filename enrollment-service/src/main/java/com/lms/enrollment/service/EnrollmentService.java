package com.lms.enrollment.service;

import com.lms.enrollment.dto.EnrollmentResponse;
import com.lms.enrollment.kafka.EnrollmentEventProducer;
import com.lms.enrollment.model.Enrollment;
import com.lms.enrollment.repository.EnrollmentRepository;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class EnrollmentService {

    private final EnrollmentRepository repo;
    private final EnrollmentEventProducer producer;

    public EnrollmentService(EnrollmentRepository repo,
                             EnrollmentEventProducer producer) {
        this.repo = repo;
        this.producer = producer;
    }

    // ============================
    // CREATE ENROLLMENT
    // ============================
    @CacheEvict(value = "enrollmentsByUser", key = "#email")
    public EnrollmentResponse enroll(Long courseId, String email) {

        if (courseId == null) {
            throw new RuntimeException("courseId is required");
        }

        Enrollment enrollment = new Enrollment();
        enrollment.setCourseId(courseId);
        enrollment.setUserId(generateUserId(email));
        enrollment.setEnrolledAt(Instant.now());

        Enrollment saved = repo.save(enrollment);

        // Kafka should never break REST
        try {
            producer.sendEvent(
                    "User " + email + " enrolled in course " + courseId
            );
        } catch (Exception e) {
            System.out.println("Kafka unavailable, skipping enrollment event");
        }

        return mapToResponse(saved);
    }

    // ============================
    // GET MY ENROLLMENTS (CACHED)
    // ============================
    @Cacheable(value = "enrollmentsByUser", key = "#email")
    public List<EnrollmentResponse> getMyEnrollments(String email) {

        Long userId = generateUserId(email);

        return repo.findByUserId(userId)
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    // ============================
    // GET BY ID
    // ============================
    @Cacheable(value = "enrollmentById", key = "#id")
    public EnrollmentResponse getEnrollment(Long id, String email) {

        Enrollment e = repo.findById(id)
                .orElseThrow(() -> new RuntimeException("Enrollment not found"));

        if (!e.getUserId().equals(generateUserId(email))) {
            throw new RuntimeException("Unauthorized");
        }

        return mapToResponse(e);
    }

    // ============================
    // DELETE
    // ============================
    @CacheEvict(value = {"enrollmentsByUser", "enrollmentById"}, allEntries = true)
    public String deleteEnrollment(Long id, String email) {

        Enrollment e = repo.findById(id)
                .orElseThrow(() -> new RuntimeException("Enrollment not found"));

        if (!e.getUserId().equals(generateUserId(email))) {
            throw new RuntimeException("Unauthorized");
        }

        repo.delete(e);

        try {
            producer.sendEvent("Enrollment deleted: " + id);
        } catch (Exception ex) {
            System.out.println("Kafka unavailable, delete event skipped");
        }

        return "Enrollment deleted";
    }

    // ============================
    // MAPPER
    // ============================
    private EnrollmentResponse mapToResponse(Enrollment e) {
        EnrollmentResponse res = new EnrollmentResponse();
        res.setEnrollmentId(e.getId());
        res.setUserId(e.getUserId());
        res.setCourseId(e.getCourseId());
        res.setEnrolledAt(e.getEnrolledAt());
        return res;
    }

    // ============================
    // USER ID STRATEGY
    // ============================
    private Long generateUserId(String email) {
        return Math.abs(email.hashCode() * 1L);
    }
}
