package com.lms.notification.consumer;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.lms.notification.dto.NotificationEvent;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class AuthEventConsumer {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @KafkaListener(topics = "auth-events", groupId = "notification-group")
    public void consume(String message) throws Exception {

        System.out.println("\n===== AUTH EVENT RECEIVED =====");
        System.out.println(message);
        System.out.println("================================");

        // You can parse JSON if needed:
        // AuthEvent event = objectMapper.readValue(message, AuthEvent.class);
    }
}
