package com.lms.assessment.dto;

public class AttemptAnswerRequest {
    private Long questionId;
    private Long selectedOptionId;

    public AttemptAnswerRequest() {}

    public Long getQuestionId() { return questionId; }
    public void setQuestionId(Long questionId) { this.questionId = questionId; }

    public Long getSelectedOptionId() { return selectedOptionId; }
    public void setSelectedOptionId(Long selectedOptionId) { this.selectedOptionId = selectedOptionId; }
}
