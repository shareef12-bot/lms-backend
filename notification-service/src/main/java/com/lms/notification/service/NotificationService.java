package com.lms.notification.service;

import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class NotificationService {

    private final KafkaTemplate<String, String> kafkaTemplate;
    private static final String TOPIC = "notifications";

    public NotificationService(KafkaTemplate<String, String> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public String sendNotification(String message) {
        kafkaTemplate.send(TOPIC, message);
        return "Notification Sent";
    }
}
