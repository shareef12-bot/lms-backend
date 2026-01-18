package com.lms.notification.consumer;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class ContentEventConsumer {

    @KafkaListener(
        topics = "${topics.content}",
        groupId = "notification-group"
    )
    public void consume(String message) {
        System.out.println("\n===== CONTENT EVENT RECEIVED =====");
        System.out.println(message);
        System.out.println("=================================\n");
    }
}
