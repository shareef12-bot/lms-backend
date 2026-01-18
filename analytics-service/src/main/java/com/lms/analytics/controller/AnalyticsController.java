package com.lms.analytics.controller;

import com.lms.analytics.model.AnalyticsEvent;
import com.lms.analytics.service.AnalyticsService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/analytics")
public class AnalyticsController {

    private final AnalyticsService service;

    public AnalyticsController(AnalyticsService service) {
        this.service = service;
    }

    @GetMapping("/health")
    public String health() {
        return "Analytics Service OK";
    }

    @GetMapping("/events")
    public List<AnalyticsEvent> getAllEvents() {
        return service.getAllEvents();
    }

    @GetMapping("/events/{type}")
    public List<AnalyticsEvent> getEventsByType(@PathVariable String type) {
        return service.getEventsByType(type);
    }

    @GetMapping("/count/{type}")
    public long countEvents(@PathVariable String type) {
        return service.countByType(type);
    }
}
