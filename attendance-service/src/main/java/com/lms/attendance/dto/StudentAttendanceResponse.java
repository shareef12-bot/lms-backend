package com.lms.attendance.dto;

import com.lms.attendance.entity.AttendanceStatus;
import java.time.LocalDate;

public class StudentAttendanceResponse {

    private LocalDate date;
    private AttendanceStatus status;

    public StudentAttendanceResponse(LocalDate date, AttendanceStatus status) {
        this.date = date;
        this.status = status;
    }

    public LocalDate getDate() {
        return date;
    }

    public AttendanceStatus getStatus() {
        return status;
    }
}
