package com.lms.batch.controller;

import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/student/batch")
public class StudentBatchController {

    @GetMapping
    public String myBatch() {
        return "Student batch details";
    }
}
