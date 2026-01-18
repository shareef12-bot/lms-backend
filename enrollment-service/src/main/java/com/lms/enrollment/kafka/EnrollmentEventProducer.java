package com.lms.enrollment.kafka;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
public class EnrollmentEventProducer {

    private final KafkaTemplate<String, String> kafkaTemplate;

    @Value("${topics.enrollment}")
    private String topic;

    public EnrollmentEventProducer(KafkaTemplate<String, String> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void sendEvent(String event) {
        kafkaTemplate.send(topic, event);
    }
}
