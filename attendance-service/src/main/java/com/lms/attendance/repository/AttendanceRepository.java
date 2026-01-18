package com.lms.attendance.repository;

import com.lms.attendance.entity.Attendance;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface AttendanceRepository extends JpaRepository<Attendance, Long> {

    Optional<Attendance> findByStudentEmailAndAttendanceDate(
            String studentEmail,
            LocalDate attendanceDate
    );

    List<Attendance> findByStudentEmailAndAttendanceDateBetween(
            String studentEmail,
            LocalDate start,
            LocalDate end
    );
}
