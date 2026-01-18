package com.lms.course.kafka;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.lms.course.dto.CourseEvent;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class CourseEventProducer {

    private final KafkaTemplate<String, String> kafkaTemplate;
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Value("${topics.course}")
    private String topic;

    public CourseEventProducer(KafkaTemplate<String, String> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void send(CourseEvent event) {
        try {
            String json = objectMapper.writeValueAsString(event);
            kafkaTemplate.send(topic, json);
            System.out.println("✔ Sent COURSE Event → " + json);
        } catch (Exception e) {
            throw new RuntimeException("Failed to send course event", e);
        }
    }
}
