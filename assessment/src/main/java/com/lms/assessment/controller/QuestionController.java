package com.lms.assessment.controller;

import com.lms.assessment.dto.CreateQuestionRequest;
import com.lms.assessment.model.Question;
import com.lms.assessment.service.QuestionService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/questions")
public class QuestionController {

    private final QuestionService questionService;

    public QuestionController(QuestionService questionService) {
        this.questionService = questionService;
    }

    @PostMapping
    public Question addQuestion(@RequestBody CreateQuestionRequest req) {
        return questionService.addQuestion(req);
    }
}
