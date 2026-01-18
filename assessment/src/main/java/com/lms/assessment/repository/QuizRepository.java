//package com.lms.assessment.repository;
//
//import com.lms.assessment.model.Quiz;
//import org.springframework.data.jpa.repository.JpaRepository;
//
//public interface QuizRepository extends JpaRepository<Quiz, Long> {
//	
//}
package com.lms.assessment.repository;

import com.lms.assessment.model.Quiz;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface QuizRepository extends JpaRepository<Quiz, Long> {

    // ONLY ACTIVE quizzes
    List<Quiz> findByActiveTrue();

    // MUST return Optional
    Optional<Quiz> findByIdAndActiveTrue(Long id);
}
