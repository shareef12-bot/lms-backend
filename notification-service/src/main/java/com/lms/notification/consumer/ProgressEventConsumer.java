package com.lms.notification.consumer;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class ProgressEventConsumer {

    @KafkaListener(topics = "${topics.progress}", groupId = "notification-group")
    public void handleProgressEvent(String message) {
        System.out.println("📩 [NOTIFICATION] PROGRESS EVENT RECEIVED → " + message);
    }
}
