package com.lms.student.service;

import com.lms.student.dto.CreateStudentRequest;
import com.lms.student.dto.StudentResponse;
import com.lms.student.kafka.StudentEventProducer;
import com.lms.student.model.Student;
import com.lms.student.repo.StudentRepository;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
import java.util.Map;

@Service
public class StudentService {

    private final StudentRepository repo;
    private final StudentEventProducer producer;

    public StudentService(StudentRepository repo,
                          StudentEventProducer producer) {
        this.repo = repo;
        this.producer = producer;
    }

    // CREATE
    public StudentResponse create(CreateStudentRequest req) {

        if (repo.existsByUserId(req.getUserId())) {
            throw new IllegalStateException("Student already exists");
        }

        Student s = new Student();
        s.setUserId(req.getUserId());
        s.setEmail(req.getEmail());
        s.setStatus("ACTIVE");

        Student saved = repo.save(s);

        producer.send("STUDENT_CREATED", Map.of(
            "studentId", saved.getId(),
            "userId", saved.getUserId(),
            "email", saved.getEmail()
        ));

        return map(saved);
    }

    // LIST
    public List<StudentResponse> list() {
        return repo.findAll().stream().map(this::map).toList();
    }

    // FIND BY USER
    public StudentResponse byUser(Long userId) {
        return repo.findByUserId(userId)
                .map(this::map)
                .orElseThrow(() ->
                        new RuntimeException("Student not found"));
    }

    // UPDATE STATUS
    public StudentResponse updateStatus(Long id, String status) {
        Student s = repo.findById(id)
                .orElseThrow(() ->
                        new RuntimeException("Student not found"));

        s.setStatus(status);
        Student saved = repo.save(s);

        producer.send("STUDENT_STATUS_CHANGED", Map.of(
            "studentId", id,
            "status", status
        ));

        return map(saved);
    }

    // TOUCH ACTIVITY
    public void touch(Long id) {
        Student s = repo.findById(id)
                .orElseThrow(() ->
                        new RuntimeException("Student not found"));

        s.setLastActiveAt(Instant.now());
        repo.save(s);
    }

    // DELETE
    public void delete(Long id) {
        repo.deleteById(id);
        producer.send("STUDENT_DELETED", Map.of("studentId", id));
    }

    private StudentResponse map(Student s) {
        StudentResponse r = new StudentResponse();
        r.setId(s.getId());
        r.setUserId(s.getUserId());
        r.setEmail(s.getEmail());
        r.setStatus(s.getStatus());
        r.setJoinedAt(s.getJoinedAt());
        r.setLastActiveAt(s.getLastActiveAt());
        return r;
    }
    
    public void createFromUser(Long userId) {

        if (repo.existsByUserId(userId)) {
            return; // already created
        }

        Student student = new Student();
        student.setUserId(userId);
        student.setStatus("ACTIVE");

        repo.save(student);

        System.out.println("Student auto-created for userId: " + userId);
    }

}
