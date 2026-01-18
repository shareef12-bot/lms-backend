//package com.lms.assessment.service;
//
//import com.lms.assessment.dto.*;
//import com.lms.assessment.kafka.AssessmentEventProducer;
//import com.lms.assessment.model.*;
//import com.lms.assessment.repository.*;
//import org.springframework.cache.annotation.CacheEvict;
//import org.springframework.cache.annotation.Cacheable;
//import org.springframework.stereotype.Service;
//
//import java.time.Instant;
//import java.util.*;
//
//@Service
//public class QuizService {
//
//    private final QuizRepository quizRepo;
//    private final QuestionRepository questionRepo;
//    private final OptionRepository optionRepo;
//    private final AttemptRepository attemptRepo;
//    private final AnswerRepository answerRepo;
//    private final AssessmentEventProducer producer;
//
//    public QuizService(
//            QuizRepository quizRepo,
//            QuestionRepository questionRepo,
//            OptionRepository optionRepo,
//            AttemptRepository attemptRepo,
//            AnswerRepository answerRepo,
//            AssessmentEventProducer producer
//    ) {
//        this.quizRepo = quizRepo;
//        this.questionRepo = questionRepo;
//        this.optionRepo = optionRepo;
//        this.attemptRepo = attemptRepo;
//        this.answerRepo = answerRepo;
//        this.producer = producer;
//    }
//
//    // WRITE → CLEAR CACHE
//    @CacheEvict(value = {"quizById", "allQuizzes"}, allEntries = true)
//    public Quiz createQuiz(Quiz quiz) {
//        return quizRepo.save(quiz);
//    }
//
//    // READ → CACHE
//    @Cacheable(value = "quizById", key = "#id")
//    public Quiz getQuiz(Long id) {
//        return quizRepo.findById(id)
//                .orElseThrow(() -> new RuntimeException("Quiz not found"));
//    }
//
//    // READ → CACHE
//    @Cacheable(value = "allQuizzes")
//    public List<Quiz> listAll() {
//        return quizRepo.findAll();
//    }
//
//    // WRITE → CLEAR CACHE
//    @CacheEvict(value = {"quizById", "allQuizzes"}, allEntries = true)
//    public void deleteQuiz(Long id) {
//        quizRepo.deleteById(id);
//    }
//
//    // ❌ NEVER CACHE (USER ANSWERS)
//    public QuizResultResponse submitAnswers(SubmitAttemptRequest req) {
//
//        Quiz quiz = quizRepo.findById(req.getQuizId())
//                .orElseThrow(() -> new RuntimeException("Quiz not found"));
//
//        int total = quiz.getQuestions().size();
//        int correct = 0;
//
//        for (AnswerRequest ar : req.getAnswers()) {
//            Option opt = optionRepo.findById(ar.getSelectedOptionId())
//                    .orElseThrow();
//            if (opt.isCorrect()) correct++;
//        }
//
//        double percentage = (correct * 100.0) / total;
//
//        QuizResultResponse res = new QuizResultResponse();
//        res.setTotalQuestions(total);
//        res.setCorrectAnswers(correct);
//        res.setPercentage(percentage);
//
//        return res;
//    }
//
//}




//package com.lms.assessment.service;
//
//import com.lms.assessment.dto.*;
//import com.lms.assessment.kafka.AssessmentEventProducer;
//import com.lms.assessment.model.*;
//import com.lms.assessment.repository.*;
//import org.springframework.cache.annotation.CacheEvict;
//import org.springframework.cache.annotation.Cacheable;
//import org.springframework.stereotype.Service;
//
//import java.util.List;
//
//@Service
//public class QuizService {
//
//    private final QuizRepository quizRepo;
//    private final QuestionRepository questionRepo;
//    private final OptionRepository optionRepo;
//    private final AttemptRepository attemptRepo;
//    private final AnswerRepository answerRepo;
//    private final AssessmentEventProducer producer;
//
//    public QuizService(
//            QuizRepository quizRepo,
//            QuestionRepository questionRepo,
//            OptionRepository optionRepo,
//            AttemptRepository attemptRepo,
//            AnswerRepository answerRepo,
//            AssessmentEventProducer producer
//    ) {
//        this.quizRepo = quizRepo;
//        this.questionRepo = questionRepo;
//        this.optionRepo = optionRepo;
//        this.attemptRepo = attemptRepo;
//        this.answerRepo = answerRepo;
//        this.producer = producer;
//    }
//
//    // =========================
//    // CREATE QUIZ (CLEAR CACHE)
//    // =========================
//    @CacheEvict(value = {"allQuizzes"}, allEntries = true)
//    public Quiz createQuiz(Quiz quiz) {
//        return quizRepo.save(quiz);
//    }
//
//    // =========================
//    // GET QUIZ BY ID
//    // ❌ CACHE REMOVED (FIX)
//    // =========================
//    public Quiz getQuiz(Long id) {
//        return quizRepo.findById(id)
//                .orElseThrow(() -> new RuntimeException("Quiz not found"));
//    }
//
//    // =========================
//    // LIST ALL QUIZZES (SAFE TO CACHE)
//    // =========================
//    @Cacheable(value = "allQuizzes")
//    public List<Quiz> listAll() {
//        return quizRepo.findAll();
//    }
//
//    // =========================
//    // DELETE QUIZ (CLEAR CACHE)
//    // =========================
//    @CacheEvict(value = {"allQuizzes"}, allEntries = true)
//    public void deleteQuiz(Long id) {
//        quizRepo.deleteById(id);
//    }
//
//    // =========================
//    // SUBMIT ANSWERS (NO CACHE)
//    // =========================
//    public QuizResultResponse submitAnswers(SubmitAttemptRequest req) {
//
//        Quiz quiz = quizRepo.findById(req.getQuizId())
//                .orElseThrow(() -> new RuntimeException("Quiz not found"));
//
//        int total = quiz.getQuestions().size();
//        int correct = 0;
//
//        for (AnswerRequest ar : req.getAnswers()) {
//            Option opt = optionRepo.findById(ar.getSelectedOptionId())
//                    .orElseThrow(() -> new RuntimeException("Option not found"));
//
//            if (opt.isCorrect()) correct++;
//        }
//
//        double percentage = (correct * 100.0) / total;
//
//        QuizResultResponse res = new QuizResultResponse();
//        res.setTotalQuestions(total);
//        res.setCorrectAnswers(correct);
//        res.setPercentage(percentage);
//
//        return res;
//    }
//}
//
//package com.lms.assessment.service;
//
//import com.lms.assessment.dto.*;
//import com.lms.assessment.kafka.AssessmentEventProducer;
//import com.lms.assessment.model.*;
//import com.lms.assessment.repository.*;
//
//import jakarta.transaction.Transactional;
//
//import org.springframework.cache.annotation.CacheEvict;
//import org.springframework.cache.annotation.Cacheable;
//import org.springframework.stereotype.Service;
//
//import java.util.ArrayList;
//import java.util.List;
//
//@Service
//public class QuizService {
//
//    private final QuizRepository quizRepo;
//    private final QuestionRepository questionRepo;
//    private final OptionRepository optionRepo;
//    private final AttemptRepository attemptRepo;
//    private final AnswerRepository answerRepo;
//    private final AssessmentEventProducer producer;
//
//    public QuizService(
//            QuizRepository quizRepo,
//            QuestionRepository questionRepo,
//            OptionRepository optionRepo,
//            AttemptRepository attemptRepo,
//            AnswerRepository answerRepo,
//            AssessmentEventProducer producer
//    ) {
//        this.quizRepo = quizRepo;
//        this.questionRepo = questionRepo;
//        this.optionRepo = optionRepo;
//        this.attemptRepo = attemptRepo;
//        this.answerRepo = answerRepo;
//        this.producer = producer;
//    }
//
//    // =========================
//    // CREATE QUIZ ONLY
//    // =========================
//    @CacheEvict(value = {"allQuizzes"}, allEntries = true)
//    public Quiz createQuiz(Quiz quiz) {
//        return quizRepo.save(quiz);
//    }
//
//    // =========================
//    // ✅ CREATE QUIZ WITH MULTIPLE QUESTIONS (NEW)
//    // =========================
//    @CacheEvict(value = {"allQuizzes"}, allEntries = true)
//    public Quiz createQuizWithQuestions(CreateQuizWithQuestionsRequest req) {
//
//        Quiz quiz = new Quiz();
//        quiz.setTitle(req.getTitle());
//        quiz.setCourseId(req.getCourseId());
//
//        List<Question> questions = new ArrayList<>();
//
//        for (CreateQuizWithQuestionsRequest.QuestionRequest qReq : req.getQuestions()) {
//
//            Question question = new Question();
//            question.setText(qReq.getText());
//            question.setQuiz(quiz);
//
//            List<Option> options = new ArrayList<>();
//
//            for (CreateOptionRequest oReq : qReq.getOptions()) {
//                Option option = new Option();
//                option.setText(oReq.getText());
//                option.setCorrect(oReq.isCorrect());
//                option.setQuestion(question);
//                options.add(option);
//            }
//
//            question.setOptions(options);
//            questions.add(question);
//        }
//
//        quiz.setQuestions(questions);
//        return quizRepo.save(quiz); // CASCADE saves all
//    }
//
//    
//        // =========================
//    // GET QUIZ BY ID (NO CACHE)
//    // =========================
////    public Quiz getQuiz(Long id) {
////        return quizRepo.findById(id)
////                .orElseThrow(() -> new RuntimeException("Quiz not found"));
////    }
//    public Quiz getQuiz(Long id) {
//        return quizRepo.findByIdAndActiveTrue(id)
//                .orElseThrow(() -> new RuntimeException("Quiz not found"));
//    }
//
//
//
//
//    // =========================
//    // LIST ALL QUIZZES
//    // =========================
//    @Cacheable(value = "allQuizzes")
//    public List<Quiz> getAllQuizzes() {
//        return quizRepo.findByActiveTrue();
//    }
//
//    // =========================
//    // DELETE QUIZ
//    // =========================
////    @CacheEvict(value = {"allQuizzes"}, allEntries = true)
////    public void deleteQuiz(Long id) {
////        quizRepo.deleteById(id);
////    }
//    @Transactional
//    public void deleteQuiz(Long quizId) {
//
//        Quiz quiz = quizRepo.findById(quizId)
//                .orElseThrow(() -> new RuntimeException("Quiz not found"));
//
//        // 🔥 Soft delete instead of real delete
//        quiz.setActive(false);
//
//        quizRepo.save(quiz);
//    }
//
//
//    // =========================
//    // SUBMIT ANSWERS
//    // =========================
//    public QuizResultResponse submitAnswers(SubmitAttemptRequest req) {
//
//        Quiz quiz = quizRepo.findById(req.getQuizId())
//                .orElseThrow(() -> new RuntimeException("Quiz not found"));
//
//        int total = quiz.getQuestions().size();
//        int correct = 0;
//
//        for (AnswerRequest ar : req.getAnswers()) {
//            Option opt = optionRepo.findById(ar.getSelectedOptionId())
//                    .orElseThrow(() -> new RuntimeException("Option not found"));
//
//            if (opt.isCorrect()) correct++;
//        }
//
//        double percentage = (correct * 100.0) / total;
//
//        QuizResultResponse res = new QuizResultResponse();
//        res.setTotalQuestions(total);
//        res.setCorrectAnswers(correct);
//        res.setPercentage(percentage);
//
//        return res;
//    }
//}



package com.lms.assessment.service;

import com.lms.assessment.dto.*;
import com.lms.assessment.kafka.AssessmentEventProducer;
import com.lms.assessment.model.*;
import com.lms.assessment.repository.*;
import jakarta.transaction.Transactional;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class QuizService {

    private final QuizRepository quizRepo;
    private final QuestionRepository questionRepo;
    private final OptionRepository optionRepo;
    private final AttemptRepository attemptRepo;
    private final AnswerRepository answerRepo;
    private final AssessmentEventProducer producer;

    public QuizService(
            QuizRepository quizRepo,
            QuestionRepository questionRepo,
            OptionRepository optionRepo,
            AttemptRepository attemptRepo,
            AnswerRepository answerRepo,
            AssessmentEventProducer producer
    ) {
        this.quizRepo = quizRepo;
        this.questionRepo = questionRepo;
        this.optionRepo = optionRepo;
        this.attemptRepo = attemptRepo;
        this.answerRepo = answerRepo;
        this.producer = producer;
    }

    // =========================
    // CREATE QUIZ
    // =========================
    @CacheEvict(value = {"allQuizzes"}, allEntries = true)
    public Quiz createQuiz(Quiz quiz) {
        return quizRepo.save(quiz);
    }

    // =========================
    // CREATE QUIZ WITH QUESTIONS
    // =========================
    @CacheEvict(value = {"allQuizzes"}, allEntries = true)
    public Quiz createQuizWithQuestions(CreateQuizWithQuestionsRequest req) {

        Quiz quiz = new Quiz();
        quiz.setTitle(req.getTitle());
        quiz.setCourseId(req.getCourseId());

        List<Question> questions = new ArrayList<>();

        for (CreateQuizWithQuestionsRequest.QuestionRequest qReq : req.getQuestions()) {

            Question question = new Question();
            question.setText(qReq.getText());
            question.setQuiz(quiz);

            List<Option> options = new ArrayList<>();

            for (CreateOptionRequest oReq : qReq.getOptions()) {
                Option option = new Option();
                option.setText(oReq.getText());
                option.setCorrect(oReq.isCorrect());
                option.setQuestion(question);
                options.add(option);
            }

            question.setOptions(options);
            questions.add(question);
        }

        quiz.setQuestions(questions);
        return quizRepo.save(quiz);
    }

    // =========================
    // STUDENT — GET QUIZ
    // =========================
    public Quiz getQuiz(Long id) {
        return quizRepo.findByIdAndActiveTrue(id)
                .orElseThrow(() -> new RuntimeException("Quiz not found"));
    }

    // =========================
    // STUDENT — LIST QUIZZES
    // =========================
    @Cacheable("allQuizzes")
    public List<Quiz> getAllQuizzes() {
        return quizRepo.findByActiveTrue();
    }

    // =========================
    // TRAINER — SOFT DELETE
    // =========================
    @Transactional
    @CacheEvict(value = {"allQuizzes"}, allEntries = true)
    public void deleteQuiz(Long quizId) {
        Quiz quiz = quizRepo.findById(quizId)
                .orElseThrow(() -> new RuntimeException("Quiz not found"));

        quiz.setActive(false);
        quizRepo.save(quiz);
    }

    // =========================
    // SUBMIT — MUST SEE DELETED QUIZ
    // =========================
    public QuizResultResponse submitAnswers(SubmitAttemptRequest req) {

        Quiz quiz = quizRepo.findById(req.getQuizId())
                .orElseThrow(() -> new RuntimeException("Quiz not found"));

        int total = quiz.getQuestions().size();
        int correct = 0;

        for (AnswerRequest ar : req.getAnswers()) {
            Option opt = optionRepo.findById(ar.getSelectedOptionId())
                    .orElseThrow(() -> new RuntimeException("Option not found"));

            if (opt.isCorrect()) correct++;
        }

        double percentage = (correct * 100.0) / total;

        QuizResultResponse res = new QuizResultResponse();
        res.setTotalQuestions(total);
        res.setCorrectAnswers(correct);
        res.setPercentage(percentage);

        return res;
    }
}

