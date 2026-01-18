package com.lms.assessment.controller;

import com.lms.assessment.dto.CreateQuizWithQuestionsRequest;
import com.lms.assessment.model.Quiz;
import com.lms.assessment.service.QuizService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/quizzes")
public class QuizController {

    private final QuizService quizService;

    public QuizController(QuizService quizService) { this.quizService = quizService; }

    @PostMapping
    public Quiz create(@RequestBody Quiz quiz) {
        return quizService.createQuiz(quiz);
    }

    @GetMapping("/{id}")
    public Quiz get(@PathVariable Long id) {
        return quizService.getQuiz(id);
    }

    @GetMapping
    public List<Quiz> list() {
        return quizService.getAllQuizzes();
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        quizService.deleteQuiz(id);
    }
    @PostMapping("/bulk")
    public Quiz createQuizWithQuestions(
            @RequestBody CreateQuizWithQuestionsRequest req) {
        return quizService.createQuizWithQuestions(req);
    }
}
