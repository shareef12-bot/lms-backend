package com.lms.assessment.dto;

public class CreateQuestionRequest {
    private Long quizId;
    private String text;

    public CreateQuestionRequest() {}

    public Long getQuizId() { return quizId; }
    public void setQuizId(Long quizId) { this.quizId = quizId; }

    public String getText() { return text; }
    public void setText(String text) { this.text = text; }
}
