package com.lms.notification.consumer;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class FileEventConsumer {

    @KafkaListener(
            topics = "${topics.file}",
            groupId = "notification-group"
    )
    public void consumeFileEvent(String message) {

        // For now just log – later we can add email / DB / websocket
        System.out.println("📁 File Event Received: " + message);
    }
}
