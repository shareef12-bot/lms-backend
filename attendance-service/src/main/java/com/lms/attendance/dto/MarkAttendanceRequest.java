package com.lms.attendance.dto;

import com.lms.attendance.entity.AttendanceStatus;

import java.time.LocalDate;

public class MarkAttendanceRequest {

    private String studentEmail;   // ✅ REQUIRED
    private Long batchId;
    private AttendanceStatus status;
    private LocalDate date;

    public String getStudentEmail() {
        return studentEmail;
    }

    public void setStudentEmail(String studentEmail) {
        this.studentEmail = studentEmail;
    }

    public Long getBatchId() {
        return batchId;
    }

    public void setBatchId(Long batchId) {
        this.batchId = batchId;
    }

    public AttendanceStatus getStatus() {
        return status;
    }

    public void setStatus(AttendanceStatus status) {
        this.status = status;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }
}
