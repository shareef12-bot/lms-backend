package com.lms.progress.controller;

import com.lms.progress.dto.ProgressRequest;
import com.lms.progress.dto.ProgressResponse;
import com.lms.progress.service.KafkaProducerService;
import com.lms.progress.service.ProgressService;

import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/progress")
public class ProgressController {

    private final ProgressService service;
    private final KafkaProducerService kafkaProducer;

    public ProgressController(ProgressService service, KafkaProducerService kafkaProducer) {
        this.service = service;
        this.kafkaProducer = kafkaProducer;
    }

    @PostMapping
    public ProgressResponse create(@RequestBody ProgressRequest req) {
        ProgressResponse response = service.create(req);

        // 🔥 SEND KAFKA EVENT ON CREATE
        kafkaProducer.sendProgressEvent(
                "User " + response.getUserId() + " started or updated course " + response.getCourseId()
        );

        return response;
    }

    @GetMapping("/{id}")
    public ProgressResponse getById(@PathVariable Long id) {
        return service.getById(id);
    }

    @GetMapping("/user/{userId}/course/{courseId}")
    public ProgressResponse getByUserAndCourse(@PathVariable Long userId, @PathVariable Long courseId) {
        return service.getByUserAndCourse(userId, courseId);
    }

    @PutMapping("/{id}")
    public ProgressResponse update(@PathVariable Long id, @RequestBody ProgressRequest req) {
        ProgressResponse response = service.update(id, req);

        // 🔥 SEND KAFKA EVENT ON PROGRESS UPDATE
        kafkaProducer.sendProgressEvent(
                "Progress updated: User " + response.getUserId() +
                " now has " + response.getProgressPercentage() +
                "% completion in course " + response.getCourseId()
        );

        return response;
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        service.delete(id);

        // 🔥 SEND KAFKA EVENT ON DELETION
        kafkaProducer.sendProgressEvent("Progress deleted for ID: " + id);
    }
}
