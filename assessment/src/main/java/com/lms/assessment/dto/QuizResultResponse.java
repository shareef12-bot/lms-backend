package com.lms.assessment.dto;

import java.util.Map;

public class QuizResultResponse {

    private Long attemptId;
    private double score;
    private double percentage;
    private int totalQuestions;
    private int correctAnswers;
    private Map<Long, Boolean> perQuestionCorrectness;

    public QuizResultResponse() {}
    public QuizResultResponse(double percentage) {
        this.percentage = percentage;
    }

    public Long getAttemptId() { return attemptId; }
    public void setAttemptId(Long attemptId) { this.attemptId = attemptId; }

    public double getScore() { return score; }
    public void setScore(double score) { this.score = score; }

    public double getPercentage() { return percentage; }
    public void setPercentage(double percentage) { this.percentage = percentage; }

    public int getTotalQuestions() { return totalQuestions; }
    public void setTotalQuestions(int totalQuestions) { this.totalQuestions = totalQuestions; }

    public int getCorrectAnswers() { return correctAnswers; }
    public void setCorrectAnswers(int correctAnswers) { this.correctAnswers = correctAnswers; }

    public Map<Long, Boolean> getPerQuestionCorrectness() { return perQuestionCorrectness; }
    public void setPerQuestionCorrectness(Map<Long, Boolean> perQuestionCorrectness) { this.perQuestionCorrectness = perQuestionCorrectness; }
}
