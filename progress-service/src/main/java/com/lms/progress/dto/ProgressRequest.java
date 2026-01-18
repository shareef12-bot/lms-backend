package com.lms.progress.dto;

import java.util.List;

public class ProgressRequest {

    private Long userId;
    private Long courseId;
    private List<Long> completedContentIds;
    private double progressPercentage;

    // ---- getters ----
    public Long getUserId() {
        return userId;
    }

    public Long getCourseId() {
        return courseId;
    }

    public List<Long> getCompletedContentIds() {
        return completedContentIds;
    }

    public double getProgressPercentage() {
        return progressPercentage;
    }

    // ---- setters ----
    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public void setCourseId(Long courseId) {
        this.courseId = courseId;
    }

    public void setCompletedContentIds(List<Long> completedContentIds) {
        this.completedContentIds = completedContentIds;
    }

    public void setProgressPercentage(double progressPercentage) {
        this.progressPercentage = progressPercentage;
    }
}
