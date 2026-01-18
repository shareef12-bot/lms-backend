package com.lms.notification.consumer;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class NotificationEnrollmentConsumer {

    @KafkaListener(topics = "enrollment-events", groupId = "notification-group")
    public void handleEnrollmentEvents(String message) {
        System.out.println("📩 Received Enrollment Event: " + message);

        // TODO: send email / push notification / SMS
    }
}
