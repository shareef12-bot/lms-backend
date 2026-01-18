package com.lms.attendance.controller;

import com.lms.attendance.dto.StudentAttendanceResponse;
import com.lms.attendance.entity.Attendance;
import com.lms.attendance.repository.AttendanceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("api/student/attendance")
public class StudentAttendanceController {

    @Autowired
    private AttendanceRepository repository;

    @GetMapping("/monthly")
    @Cacheable(value = "monthlyAttendance", key = "#email + '-' + #year + '-' + #month")
    public List<StudentAttendanceResponse> getMonthlyAttendance(
            @RequestParam int year,
            @RequestParam int month
    ) {

        String email = (String) SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getPrincipal();

        LocalDate start = LocalDate.of(year, month, 1);
        LocalDate end = start.withDayOfMonth(start.lengthOfMonth());

        List<Attendance> records =
                repository.findByStudentEmailAndAttendanceDateBetween(
                        email, start, end
                );

        return records.stream()
                .map(a -> new StudentAttendanceResponse(
                        a.getAttendanceDate(),
                        a.getStatus()
                ))
                .collect(Collectors.toList());
    }
}
