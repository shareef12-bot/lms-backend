package com.lms.attendance.service;

import com.lms.attendance.entity.Attendance;
import com.lms.attendance.entity.AttendanceStatus;
import com.lms.attendance.repository.AttendanceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Service
public class AttendanceService {

    @Autowired
    private AttendanceRepository repository;

    @CacheEvict(value = "monthlyAttendance", allEntries = true)
    public void markAttendance(
            String studentEmail,
            Long batchId,
            AttendanceStatus status,
            LocalDate date,
            String trainerEmail
    ) {

        Attendance attendance = repository
                .findByStudentEmailAndAttendanceDate(studentEmail, date)
                .orElse(new Attendance());

        attendance.setStudentEmail(studentEmail);
        attendance.setBatchId(batchId);
        attendance.setAttendanceDate(date);
        attendance.setStatus(status);
        attendance.setMarkedByTrainerEmail(trainerEmail);
        attendance.setCreatedAt(LocalDateTime.now());

        repository.save(attendance);
    }
}
