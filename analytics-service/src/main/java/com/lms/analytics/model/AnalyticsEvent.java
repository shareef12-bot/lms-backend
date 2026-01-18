package com.lms.analytics.model;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "analytics_events")
public class AnalyticsEvent {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String eventType;
    private String message;
    private LocalDateTime timestamp;

    public AnalyticsEvent() {}

    public AnalyticsEvent(String eventType, String message, LocalDateTime timestamp) {
        this.eventType = eventType;
        this.message = message;
        this.timestamp = timestamp;
    }

    public Long getId() { return id; }

    public String getEventType() { return eventType; }
    public void setEventType(String eventType) { this.eventType = eventType; }

    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }

    public LocalDateTime getTimestamp() { return timestamp; }
    public void setTimestamp(LocalDateTime timestamp) { this.timestamp = timestamp; }
}
