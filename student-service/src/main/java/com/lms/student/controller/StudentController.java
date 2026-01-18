package com.lms.student.controller;

import com.lms.student.dto.CreateStudentRequest;
import com.lms.student.dto.StudentResponse;
import com.lms.student.service.StudentService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/students")
public class StudentController {

    private final StudentService service;

    public StudentController(StudentService service) {
        this.service = service;
    }

    @PostMapping
    public StudentResponse create(@Valid @RequestBody CreateStudentRequest req) {
        return service.create(req);
    }

    @GetMapping
    public List<StudentResponse> list() {
        return service.list();
    }

    @GetMapping("/user/{userId}")
    public StudentResponse byUser(@PathVariable Long userId) {
        return service.byUser(userId);
    }

    @PutMapping("/{id}/status")
    public StudentResponse status(
            @PathVariable Long id,
            @RequestParam String status) {
        return service.updateStatus(id, status);
    }

    @PutMapping("/{id}/touch")
    public void touch(@PathVariable Long id) {
        service.touch(id);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        service.delete(id);
    }
}
