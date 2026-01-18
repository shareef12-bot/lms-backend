package com.lms.assessment.repository;

import com.lms.assessment.model.Option;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OptionRepository extends JpaRepository<Option, Long> {
    List<Option> findByQuestion_Id(Long questionId);
}
