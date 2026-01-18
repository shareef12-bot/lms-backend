package com.lms.file.model;

import jakarta.persistence.*;
import java.time.Instant;

@Entity
@Table(name = "files")
public class FileResource {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String originalName;
    private String storedName;
    private String contentType;
    private long size;

    private String uploadedByRole; // ADMIN

    private Instant uploadedAt;

    // ===== GETTERS & SETTERS =====

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getOriginalName() { return originalName; }
    public void setOriginalName(String originalName) { this.originalName = originalName; }

    public String getStoredName() { return storedName; }
    public void setStoredName(String storedName) { this.storedName = storedName; }

    public String getContentType() { return contentType; }
    public void setContentType(String contentType) { this.contentType = contentType; }

    public long getSize() { return size; }
    public void setSize(long size) { this.size = size; }

    public String getUploadedByRole() { return uploadedByRole; }
    public void setUploadedByRole(String uploadedByRole) { this.uploadedByRole = uploadedByRole; }

    public Instant getUploadedAt() { return uploadedAt; }
    public void setUploadedAt(Instant uploadedAt) { this.uploadedAt = uploadedAt; }
}
