package com.lms.course.service;

import com.lms.course.dto.CourseEvent;
import com.lms.course.kafka.CourseEventProducer;
import com.lms.course.model.Course;
import com.lms.course.repository.CourseRepository;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class CourseService {

    private final CourseRepository repo;
    private final CourseEventProducer producer;

    public CourseService(CourseRepository repo, CourseEventProducer producer) {
        this.repo = repo;
        this.producer = producer;
    }

    // ============================
    // CREATE COURSE
    // ============================
    @CacheEvict(value = "coursesByEmail", key = "#email")
    public Course create(Course course, String email) {

        course.setOwnerEmail(email);

        Course saved = repo.save(course);

        // 🔥 Kafka must NEVER break API
        try {
            producer.send(new CourseEvent(
                    "COURSE_CREATED",
                    Map.of(
                            "courseId", saved.getId(),
                            "title", saved.getTitle(),
                            "ownerEmail", saved.getOwnerEmail()
                    )
            ));
        } catch (Exception e) {
            System.out.println("Kafka unavailable, skipping COURSE_CREATED event");
        }

        return saved;
    }

    // ============================
    // GET COURSES BY OWNER (CACHED)
    // ============================
    @Cacheable(value = "coursesByEmail", key = "#email")
    public List<Course> getByEmail(String email) {
        return repo.findByOwnerEmail(email);
    }

    // ============================
    // LIST ALL COURSES (CACHED)
    // ============================
    @Cacheable(value = "allCourses")
    public List<Course> listAll() {
        return repo.findAll();
    }

    // ============================
    // GET BY ID (CACHED)
    // ============================
    @Cacheable(value = "courseById", key = "#id")
    public Course getById(Long id) {
        return repo.findById(id)
                .orElseThrow(() -> new RuntimeException("Course not found with id " + id));
    }

    // ============================
    // UPDATE COURSE
    // ============================
    @CacheEvict(value = {"courseById", "coursesByEmail", "allCourses"}, allEntries = true)
    public Course update(Long id, Course updated) {

        Course existing = getById(id);

        if (updated.getTitle() != null)
            existing.setTitle(updated.getTitle());

        if (updated.getDescription() != null)
            existing.setDescription(updated.getDescription());

        if (updated.getCategory() != null)
            existing.setCategory(updated.getCategory());

        return repo.save(existing);
    }

    // ============================
    // DELETE COURSE
    // ============================
    @CacheEvict(value = {"courseById", "coursesByEmail", "allCourses"}, allEntries = true)
    public String delete(Long id) {
        repo.deleteById(id);
        return "Course deleted successfully";
    }
}
