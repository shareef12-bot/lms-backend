package com.lms.enrollment.controller;

import com.lms.enrollment.dto.EnrollmentRequest;
import com.lms.enrollment.dto.EnrollmentResponse;
import com.lms.enrollment.service.EnrollmentService;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/enrollments")
@CrossOrigin(origins = "http://localhost:5173")
public class EnrollmentController {

    private final EnrollmentService service;

    public EnrollmentController(EnrollmentService service) {
        this.service = service;
    }

    // 🔐 ENROLL CURRENT USER
    @PostMapping
    public EnrollmentResponse enroll(
            @RequestBody EnrollmentRequest req,
            Authentication auth
    ) {
        return service.enroll(req.getCourseId(), auth.getName());
    }

    // 🔐 GET MY ENROLLMENTS (REDIS CACHED)
    @GetMapping("/my")
    public List<EnrollmentResponse> myEnrollments(Authentication auth) {
        return service.getMyEnrollments(auth.getName());
    }

    // 🔐 GET ENROLLMENT BY ID
    @GetMapping("/{id}")
    public EnrollmentResponse getById(
            @PathVariable Long id,
            Authentication auth
    ) {
        return service.getEnrollment(id, auth.getName());
    }

    // 🔐 DELETE ENROLLMENT
    @DeleteMapping("/{id}")
    public String delete(
            @PathVariable Long id,
            Authentication auth
    ) {
        return service.deleteEnrollment(id, auth.getName());
    }
}
