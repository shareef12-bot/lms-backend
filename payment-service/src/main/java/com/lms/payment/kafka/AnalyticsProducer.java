package com.lms.payment.kafka;

import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class AnalyticsProducer {
    private final KafkaTemplate<String, String> kafkaTemplate;
    public AnalyticsProducer(KafkaTemplate<String, String> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }
    public void sendAnalytics(String topic, String message) {
        kafkaTemplate.send(topic, message);
    }
}
