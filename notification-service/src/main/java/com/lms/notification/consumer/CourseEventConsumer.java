package com.lms.notification.consumer;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class CourseEventConsumer {

    @KafkaListener(
        topics = "${topics.course}",
        groupId = "notification-group"
    )
    public void consume(String message) {
        System.out.println("\n===== COURSE EVENT RECEIVED =====");
        System.out.println(message);
        System.out.println("================================\n");
    }
}
