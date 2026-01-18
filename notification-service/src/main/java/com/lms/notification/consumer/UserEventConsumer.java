package com.lms.notification.consumer;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class UserEventConsumer {

    @KafkaListener(topics = "user-events", groupId = "notification-group")
    public void consumeUserEvent(String message) {

        System.out.println("\n===== USER EVENT RECEIVED =====");
        System.out.println(message);
        System.out.println("================================\n");
    }
}
