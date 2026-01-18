//package com.lms.assessment.service;
//
//import com.lms.assessment.dto.AnswerRequest;
//import org.springframework.security.core.Authentication;
//import org.springframework.security.core.context.SecurityContextHolder;
//
//import com.lms.assessment.dto.SubmitAttemptRequest;
//import com.lms.assessment.model.Attempt;
//import com.lms.assessment.model.Option;
//import com.lms.assessment.model.Quiz;
//import com.lms.assessment.repository.AttemptRepository;
//import com.lms.assessment.repository.OptionRepository;
//import com.lms.assessment.repository.QuizRepository;
//
//import org.springframework.stereotype.Service;
//
//import java.time.Instant;
//
//
//@Service
//public class AttemptService {
//
//    private final AttemptRepository attemptRepo;
//    private final QuizRepository quizRepo;
//    private final OptionRepository optionRepo;
//
//    public AttemptService(AttemptRepository attemptRepo,
//                          QuizRepository quizRepo,
//                          OptionRepository optionRepo) {
//        this.attemptRepo = attemptRepo;
//        this.quizRepo = quizRepo;
//        this.optionRepo = optionRepo;
//    }
//
////    public Attempt submitAttempt(SubmitAttemptRequest req) {
////
////        Quiz quiz = quizRepo.findById(req.getQuizId())
////                .orElseThrow(() -> new RuntimeException("Quiz not found: " + req.getQuizId()));
////
////        int score = 0;
////
////        // FIX: Use AnswerRequest instead of AttemptAnswerRequest
////        if (req.getAnswers() != null) {
////            for (AnswerRequest ans : req.getAnswers()) {
////                if (ans.getSelectedOptionId() == null) continue;
////
////                Option selected = optionRepo.findById(ans.getSelectedOptionId())
////                        .orElseThrow(() -> new RuntimeException("Option not found: " + ans.getSelectedOptionId()));
////
////                if (selected.isCorrect()) score++;
////            }
////        }
////
////        Attempt attempt = new Attempt();
////        attempt.setQuiz(quiz);
////        attempt.setUserId(req.getUserId());
////        attempt.setScore(score);
////        attempt.setStartedAt(Instant.now());
////        attempt.setCompletedAt(Instant.now());
////
////        return attemptRepo.save(attempt);
////    }
////
//    public Attempt submitAttempt(SubmitAttemptRequest req) {
//
//        // 🔐 Get logged-in user from JWT
//        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
//        Long userId = Long.valueOf(auth.getName()); 
//        // auth.getName() should contain USER ID (not email)
//
//        Quiz quiz = quizRepo.findById(req.getQuizId())
//                .orElseThrow(() -> new RuntimeException("Quiz not found"));
//
//        int score = 0;
//
//        if (req.getAnswers() != null) {
//            for (AnswerRequest ans : req.getAnswers()) {
//                if (ans.getSelectedOptionId() == null) continue;
//
//                Option selected = optionRepo.findById(ans.getSelectedOptionId())
//                        .orElseThrow(() -> new RuntimeException("Option not found"));
//
//                if (selected.isCorrect()) score++;
//            }
//        }
//
//        Attempt attempt = new Attempt();
//        attempt.setQuiz(quiz);
//        attempt.setUserId(userId); // ✅ SAFE & CORRECT
//        attempt.setScore(score);
//        attempt.setStartedAt(Instant.now());
//        attempt.setCompletedAt(Instant.now());
//
//        return attemptRepo.save(attempt);
//    }
//
//    
//    
//    public Attempt getAttempt(Long id) {
//        return attemptRepo.findById(id)
//                .orElseThrow(() -> new RuntimeException("Attempt not found: " + id));
//    }
//}

//package com.lms.assessment.service;
//
//import com.lms.assessment.dto.AnswerRequest;
//import com.lms.assessment.dto.SubmitAttemptRequest;
//import com.lms.assessment.model.Attempt;
//import com.lms.assessment.model.Option;
//import com.lms.assessment.model.Quiz;
//import com.lms.assessment.repository.AttemptRepository;
//import com.lms.assessment.repository.OptionRepository;
//import com.lms.assessment.repository.QuizRepository;
//
//import org.springframework.security.core.Authentication;
//import org.springframework.security.core.context.SecurityContextHolder;
//import org.springframework.stereotype.Service;
//
//import java.time.Instant;
//
//@Service
//public class AttemptService {
//
//    private final AttemptRepository attemptRepo;
//    private final QuizRepository quizRepo;
//    private final OptionRepository optionRepo;
//
//    public AttemptService(AttemptRepository attemptRepo,
//                          QuizRepository quizRepo,
//                          OptionRepository optionRepo) {
//        this.attemptRepo = attemptRepo;
//        this.quizRepo = quizRepo;
//        this.optionRepo = optionRepo;
//    }
//
//    public Attempt submitAttempt(SubmitAttemptRequest req) {
//
//        // 🔐 Get logged-in user from JWT
//        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
//        Long userId = Long.valueOf(auth.getName()); // MUST be userId in JWT
//        System.out.println("AUTH NAME = " + auth.getName());
//        System.out.println("ROLE = " + auth.getAuthorities());
//
//        // 🚫 PREVENT MULTIPLE ATTEMPTS (NEW – SAFE)
////        attemptRepo.findByQuiz_IdAndUserId(req.getQuizId(), userId)
////                .ifPresent(a -> {
////                    throw new RuntimeException("You already attempted this quiz");
////                });
//        attemptRepo.findByQuiz_IdAndUserId(req.getQuizId(), userId)
//        .ifPresent(a -> {
//
//            Instant lastSubmit = a.getSubmittedAt();   // saved in step 2
//            Instant now = Instant.now();
//
//            // 24 hours = 86400 seconds
//            long hoursPassed = java.time.Duration.between(lastSubmit, now).toHours();
//
//            if (hoursPassed < 24) {
//                throw new RuntimeException(
//                        "You can reattempt this quiz after 24 hours"
//                );
//            }
//        });
//
//
//        // ✅ EXISTING LOGIC (UNCHANGED)
//        Quiz quiz = quizRepo.findById(req.getQuizId())
//                .orElseThrow(() -> new RuntimeException("Quiz not found"));
//
//        int score = 0;
//
//        if (req.getAnswers() != null) {
//            for (AnswerRequest ans : req.getAnswers()) {
//                if (ans.getSelectedOptionId() == null) continue;
//
//                Option selected = optionRepo.findById(ans.getSelectedOptionId())
//                        .orElseThrow(() -> new RuntimeException("Option not found"));
//
//                if (selected.isCorrect()) score++;
//            }
//        }
//
//        Attempt attempt = new Attempt();
//        attempt.setQuiz(quiz);
//        attempt.setUserId(userId);
//        attempt.setScore(score);
//        attempt.setStartedAt(Instant.now());
//        attempt.setCompletedAt(Instant.now());
//        attempt.setSubmittedAt(Instant.now());
//        return attemptRepo.save(attempt);
//    }
//
//    public Attempt getAttempt(Long id) {
//        return attemptRepo.findById(id)
//                .orElseThrow(() -> new RuntimeException("Attempt not found: " + id));
//    }
//    public boolean hasUserAttempted(Long quizId) {
//        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
//        Long userId = Long.valueOf(auth.getName());
//
//        attemptRepo.findByQuiz_IdAndUserEmail(req.getQuizId(), userEmail)
//
//    }
//
//}
//package com.lms.assessment.service;
//
//import com.lms.assessment.dto.AnswerRequest;
//import com.lms.assessment.dto.SubmitAttemptRequest;
//import com.lms.assessment.model.Attempt;
//import com.lms.assessment.model.Option;
//import com.lms.assessment.model.Quiz;
//import com.lms.assessment.repository.AttemptRepository;
//import com.lms.assessment.repository.OptionRepository;
//import com.lms.assessment.repository.QuizRepository;
//
//import org.springframework.security.core.Authentication;
//import org.springframework.security.core.context.SecurityContextHolder;
//import org.springframework.stereotype.Service;
//
//import java.time.Duration;
//import java.time.Instant;
//
//@Service
//public class AttemptService {
//
//    private final AttemptRepository attemptRepo;
//    private final QuizRepository quizRepo;
//    private final OptionRepository optionRepo;
//
//    public AttemptService(AttemptRepository attemptRepo,
//                          QuizRepository quizRepo,
//                          OptionRepository optionRepo) {
//        this.attemptRepo = attemptRepo;
//        this.quizRepo = quizRepo;
//        this.optionRepo = optionRepo;
//    }
//
//    // =========================
//    // SUBMIT QUIZ ATTEMPT
//    // =========================
//    public Attempt submitAttempt(SubmitAttemptRequest req) {
//
//        // 🔐 Get logged-in user (EMAIL from JWT)
//        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
//        String userEmail = auth.getName();   // this is email in JWT
//
//        System.out.println("JWT USER = " + auth.getName());
//        System.out.println("JWT AUTHORITIES = " + auth.getAuthorities());
//
//        // 🚫 24 HOUR LOCK CHECK
//        attemptRepo.findByQuiz_IdAndUserEmail(req.getQuizId(), userEmail)
//                .ifPresent(a -> {
//
//                    Instant lastSubmit = a.getSubmittedAt();
//                    Instant now = Instant.now();
//
//                    long hoursPassed = Duration.between(lastSubmit, now).toHours();
//
//                    if (hoursPassed < 24) {
//                        throw new RuntimeException("You can reattempt this quiz after 24 hours");
//                    }
//                });
//
//        // ======================
//        // LOAD QUIZ
//        // ======================
//        Quiz quiz = quizRepo.findById(req.getQuizId())
//                .orElseThrow(() -> new RuntimeException("Quiz not found"));
//
//        int score = 0;
//
//        if (req.getAnswers() != null) {
//            for (AnswerRequest ans : req.getAnswers()) {
//                if (ans.getSelectedOptionId() == null) continue;
//
//                Option selected = optionRepo.findById(ans.getSelectedOptionId())
//                        .orElseThrow(() -> new RuntimeException("Option not found"));
//
//                if (selected.isCorrect()) score++;
//            }
//        }
//
//        // ======================
//        // SAVE ATTEMPT
//        // ======================
//        Attempt attempt = new Attempt();
//        attempt.setQuiz(quiz);
//        attempt.setUserEmail(userEmail);
//        attempt.setScore(score);
//        attempt.setStartedAt(Instant.now());
//        attempt.setCompletedAt(Instant.now());
//        attempt.setSubmittedAt(Instant.now());
//
//        return attemptRepo.save(attempt);
//    }
//
//    // =========================
//    // GET ATTEMPT
//    // =========================
//    public Attempt getAttempt(Long id) {
//        return attemptRepo.findById(id)
//                .orElseThrow(() -> new RuntimeException("Attempt not found: " + id));
//    }
//
//    // =========================
//    // CHECK IF USER ATTEMPTED
//    // =========================
//    public boolean hasUserAttempted(Long quizId) {
//
//        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
//        String userEmail = auth.getName();   // email from JWT
//
//        return attemptRepo.existsByQuiz_IdAndUserEmail(quizId, userEmail);
//    }
//}
//
//


package com.lms.assessment.service;

import com.lms.assessment.dto.AnswerRequest;
import com.lms.assessment.dto.SubmitAttemptRequest;
import com.lms.assessment.model.Attempt;
import com.lms.assessment.model.Option;
import com.lms.assessment.model.Quiz;
import com.lms.assessment.repository.AttemptRepository;
import com.lms.assessment.repository.OptionRepository;
import com.lms.assessment.repository.QuizRepository;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.Instant;

@Service
public class AttemptService {

    private final AttemptRepository attemptRepo;
    private final QuizRepository quizRepo;
    private final OptionRepository optionRepo;

    public AttemptService(
            AttemptRepository attemptRepo,
            QuizRepository quizRepo,
            OptionRepository optionRepo
    ) {
        this.attemptRepo = attemptRepo;
        this.quizRepo = quizRepo;
        this.optionRepo = optionRepo;
    }

    // =========================
    // SUBMIT QUIZ
    // =========================
    public Attempt submitAttempt(SubmitAttemptRequest req) {

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        if (auth == null || auth.getName() == null || auth.getName().isBlank()) {
            throw new RuntimeException("JWT not found — user not authenticated");
        }

        String userEmail = auth.getName();   // email from JWT

        System.out.println("ATTEMPT USER = " + userEmail);

        // 🔐 24-hour lock
        attemptRepo.findByQuiz_IdAndUserEmail(req.getQuizId(), userEmail)
                .ifPresent(a -> {
                    Instant last = a.getSubmittedAt();
                    long hours = Duration.between(last, Instant.now()).toHours();

                    if (hours < 24) {
                        throw new RuntimeException("You can reattempt this quiz after 24 hours");
                    }
                });

        Quiz quiz = quizRepo.findById(req.getQuizId())
                .orElseThrow(() -> new RuntimeException("Quiz not found"));

        int score = 0;

        if (req.getAnswers() != null) {
            for (AnswerRequest ans : req.getAnswers()) {
                if (ans.getSelectedOptionId() == null) continue;

                Option selected = optionRepo.findById(ans.getSelectedOptionId())
                        .orElseThrow(() -> new RuntimeException("Option not found"));

                if (selected.isCorrect()) score++;
            }
        }

        Attempt attempt = new Attempt();
        attempt.setQuiz(quiz);
        attempt.setUserEmail(userEmail);
        attempt.setScore(score);
        attempt.setStartedAt(Instant.now());
        attempt.setCompletedAt(Instant.now());
        attempt.setSubmittedAt(Instant.now());

        Attempt saved = attemptRepo.save(attempt);

        System.out.println("ATTEMPT SAVED → " + saved.getId());

        return saved;
    }

    // =========================
    // CHECK IF ATTEMPTED
    // =========================
    public boolean hasUserAttempted(Long quizId) {

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        if (auth == null || auth.getName() == null) {
            return false;
        }

        String userEmail = auth.getName();

        return attemptRepo.existsByQuiz_IdAndUserEmail(quizId, userEmail);
    }

    public Attempt getAttempt(Long id) {
        return attemptRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Attempt not found"));
    }
 // =========================
 // TRAINER: GET ALL ATTEMPTS FOR A QUIZ
 // =========================
 public java.util.List<Attempt> getAttemptsForQuiz(Long quizId) {
     return attemptRepo.findByQuiz_Id(quizId);
 }
//=========================
//STUDENT: GET MY ATTEMPTS
//=========================
public java.util.List<Attempt> getMyAttempts() {

  Authentication auth = SecurityContextHolder.getContext().getAuthentication();

  if (auth == null || auth.getName() == null || auth.getName().isBlank()) {
      throw new RuntimeException("JWT not found — user not authenticated");
  }

  String userEmail = auth.getName();

  return attemptRepo.findByUserEmailOrderBySubmittedAtDesc(userEmail);
}

 
 
}
