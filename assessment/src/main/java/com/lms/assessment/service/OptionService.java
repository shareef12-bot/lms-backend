package com.lms.assessment.service;

import com.lms.assessment.dto.CreateOptionRequest;
import com.lms.assessment.model.Option;
import com.lms.assessment.model.Question;
import com.lms.assessment.repository.OptionRepository;
import com.lms.assessment.repository.QuestionRepository;
import org.springframework.stereotype.Service;

@Service
public class OptionService {

    private final OptionRepository optionRepo;
    private final QuestionRepository questionRepo;

    public OptionService(OptionRepository optionRepo, QuestionRepository questionRepo) {
        this.optionRepo = optionRepo;
        this.questionRepo = questionRepo;
    }

    public Option addOption(CreateOptionRequest req) {

        Question question = questionRepo.findById(req.getQuestionId())
                .orElseThrow(() -> new RuntimeException("Question not found: " + req.getQuestionId()));

        Option op = new Option();
        op.setQuestion(question);
        op.setText(req.getText());
        op.setCorrect(req.isCorrect());

        return optionRepo.save(op);
    }
}
