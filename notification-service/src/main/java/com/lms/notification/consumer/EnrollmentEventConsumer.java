package com.lms.notification.consumer;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.lms.notification.dto.NotificationEvent;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class EnrollmentEventConsumer {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @KafkaListener(
            topics = "${topics.enrollment}",
            groupId = "notification-group"
    )
    public void consume(String message) throws Exception {

        System.out.println("\n===== ENROLLMENT EVENT RECEIVED =====");
        System.out.println(message);
        System.out.println("====================================");

        // 🔹 Optional: parse JSON when you move to structured events
        // NotificationEvent event =
        //         objectMapper.readValue(message, NotificationEvent.class);

        // 🔔 Later you can trigger:
        // - Email
        // - SMS
        // - Push notification
    }
}
