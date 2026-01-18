package com.lms.course.kafka;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.lms.course.dto.ContentEvent;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class ContentEventProducer {

    private final KafkaTemplate<String, String> kafkaTemplate;
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Value("${topics.content}")
    private String topic;

    public ContentEventProducer(KafkaTemplate<String, String> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void sendEvent(ContentEvent event) {
        try {
            String json = objectMapper.writeValueAsString(event);
            kafkaTemplate.send(topic, json);
            System.out.println("📤 Sent CONTENT Event → " + json);
        } catch (Exception e) {
            throw new RuntimeException("Failed to send content event", e);
        }
    }
}
