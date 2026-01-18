package com.lms.search.model;

import jakarta.persistence.*;

@Entity
@Table(name = "search_index")
public class SearchIndex {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long courseId;
    private String courseTitle;
    private String courseDescription;

    // Getters & Setters
    public Long getId() { return id; }
    public Long getCourseId() { return courseId; }
    public String getCourseTitle() { return courseTitle; }
    public String getCourseDescription() { return courseDescription; }

    public void setId(Long id) { this.id = id; }
    public void setCourseId(Long courseId) { this.courseId = courseId; }
    public void setCourseTitle(String courseTitle) { this.courseTitle = courseTitle; }
    public void setCourseDescription(String courseDescription) { this.courseDescription = courseDescription; }
}
