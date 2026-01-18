package com.lms.attendance.controller;

import com.lms.attendance.dto.MarkAttendanceRequest;
import com.lms.attendance.service.AttendanceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/trainer/attendance")
public class TrainerAttendanceController {

    @Autowired
    private AttendanceService attendanceService;

    @PostMapping("/mark")
    public ResponseEntity<String> markAttendance(
            @RequestBody MarkAttendanceRequest request
    ) {

        String trainerEmail = (String) SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getPrincipal();

        attendanceService.markAttendance(
                request.getStudentEmail(),   // ✅ STRING
                request.getBatchId(),
                request.getStatus(),
                request.getDate(),
                trainerEmail
        );

        return ResponseEntity.ok("Attendance marked successfully");
    }
}
