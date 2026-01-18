package com.lms.assessment.dto;

public class CreateOptionRequest {
    private Long questionId;
    private String text;
    private boolean correct;

    public CreateOptionRequest() {}

    public Long getQuestionId() { return questionId; }
    public void setQuestionId(Long questionId) { this.questionId = questionId; }

    public String getText() { return text; }
    public void setText(String text) { this.text = text; }

    public boolean isCorrect() { return correct; }
    public void setCorrect(boolean correct) { this.correct = correct; }
}
