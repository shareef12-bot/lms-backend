package com.lms.progress.service;

import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class KafkaProducerService {

    private final KafkaTemplate<String, String> kafkaTemplate;

    @Value("${topics.progress}")
    private String progressTopic;

    public KafkaProducerService(KafkaTemplate<String, String> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void sendProgressEvent(String message) {
        kafkaTemplate.send(progressTopic, message);
        System.out.println("🔥 Sent Kafka message: " + message);
    }
}
