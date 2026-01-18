package com.lms.notification.consumer;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.lms.notification.dto.NotificationEvent;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class AssessmentEventConsumer {

    private final ObjectMapper mapper = new ObjectMapper();

    @KafkaListener(topics = "assessment-events", groupId = "notification-group")
    public void consume(String message) throws Exception {

        System.out.println("\n===== ASSESSMENT EVENT RECEIVED =====");
        System.out.println(message);
        System.out.println("=====================================\n");

        // Optionally parse JSON:
        // NotificationEvent event = mapper.readValue(message, NotificationEvent.class);
    }
}
