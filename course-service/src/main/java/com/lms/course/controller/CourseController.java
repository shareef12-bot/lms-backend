package com.lms.course.controller;

import com.lms.course.model.Course;
import com.lms.course.service.CourseService;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/courses")
public class CourseController {

    private final CourseService service;

    public CourseController(CourseService service) {
        this.service = service;
    }

    // 🔐 CREATE COURSE (JWT REQUIRED)
    @PostMapping
    public Course create(@RequestBody Course course, Authentication auth) {

        if (auth == null || auth.getName() == null) {
            throw new RuntimeException("Unauthorized");
        }

        return service.create(course, auth.getName());
    }

    // 🔐 COURSES OF LOGGED-IN USER
    @GetMapping("/my")
    public List<Course> myCourses(Authentication auth) {

        if (auth == null || auth.getName() == null) {
            throw new RuntimeException("Unauthorized");
        }

        return service.getByEmail(auth.getName());
    }

    // 🔓 LIST ALL COURSES
    @GetMapping
    public List<Course> listAll() {
        return service.listAll();
    }

    // 🔓 GET BY ID
    @GetMapping("/{id}")
    public Course getById(@PathVariable Long id) {
        return service.getById(id);
    }

    // 🔐 UPDATE COURSE
    @PutMapping("/{id}")
    public Course update(@PathVariable Long id,
                         @RequestBody Course updated) {
        return service.update(id, updated);
    }

    // 🔐 DELETE COURSE
    @DeleteMapping("/{id}")
    public String delete(@PathVariable Long id) {
        return service.delete(id);
    }
}
