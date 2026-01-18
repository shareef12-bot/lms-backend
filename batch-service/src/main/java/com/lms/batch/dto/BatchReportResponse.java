package com.lms.batch.dto;

public class BatchReportResponse {

    private String batchName;
    private int students;
    private double avgScore;
    private double completion;

    public String getBatchName() {
        return batchName;
    }

    public void setBatchName(String batchName) {
        this.batchName = batchName;
    }

    public int getStudents() {
        return students;
    }

    public void setStudents(int students) {
        this.students = students;
    }

    public double getAvgScore() {
        return avgScore;
    }

    public void setAvgScore(double avgScore) {
        this.avgScore = avgScore;
    }

    public double getCompletion() {
        return completion;
    }

    public void setCompletion(double completion) {
        this.completion = completion;
    }
}
