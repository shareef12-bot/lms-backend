package com.lms.student.dto;

import java.time.Instant;

public class StudentResponse {

    private Long id;
    private Long userId;
    private String status;
    private Instant joinedAt;
    private Instant lastActiveAt;
    private String email; 

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public Instant getJoinedAt() { return joinedAt; }
    public void setJoinedAt(Instant joinedAt) { this.joinedAt = joinedAt; }

    public Instant getLastActiveAt() { return lastActiveAt; }
    public void setLastActiveAt(Instant lastActiveAt) {
        this.lastActiveAt = lastActiveAt;
    }
    public String getEmail() {            // ✅ NEW
        return email;
    }

    public void setEmail(String email) {  // ✅ NEW
        this.email = email;
    }
}
