//package com.lms.assessment.service;
//
//import com.lms.assessment.dto.CreateQuestionRequest;
//import com.lms.assessment.model.Question;
//import com.lms.assessment.model.Quiz;
//import com.lms.assessment.repository.QuestionRepository;
//import com.lms.assessment.repository.QuizRepository;
//import org.springframework.stereotype.Service;
//
//@Service
//public class QuestionService {
//
//    private final QuizRepository quizRepo;
//    private final QuestionRepository questionRepo;
//
//    public QuestionService(QuizRepository quizRepo, QuestionRepository questionRepo) {
//        this.quizRepo = quizRepo;
//        this.questionRepo = questionRepo;
//    }
//
//    public Question addQuestion(CreateQuestionRequest req) {
//
//        Quiz quiz = quizRepo.findById(req.getQuizId())
//                .orElseThrow(() -> new RuntimeException("Quiz not found: " + req.getQuizId()));
//
//        Question q = new Question();
//        q.setQuiz(quiz);
//        q.setText(req.getText());
//
//        return questionRepo.save(q);
//    }
//}
package com.lms.assessment.service;

import com.lms.assessment.dto.CreateQuestionRequest;
import com.lms.assessment.model.Question;
import com.lms.assessment.model.Quiz;
import com.lms.assessment.repository.QuestionRepository;
import com.lms.assessment.repository.QuizRepository;
import org.springframework.stereotype.Service;

@Service
public class QuestionService {

    private final QuizRepository quizRepo;
    private final QuestionRepository questionRepo;

    public QuestionService(QuizRepository quizRepo, QuestionRepository questionRepo) {
        this.quizRepo = quizRepo;
        this.questionRepo = questionRepo;
    }

    public Question addQuestion(CreateQuestionRequest req) {

        Quiz quiz = quizRepo.findById(req.getQuizId())
                .orElseThrow(() -> new RuntimeException("Quiz not found: " + req.getQuizId()));

        Question q = new Question();
        q.setText(req.getText());
        q.setQuiz(quiz);

        // ✅ CRITICAL FIX (this line was missing)
        quiz.getQuestions().add(q);

        return questionRepo.save(q);
    }
}
