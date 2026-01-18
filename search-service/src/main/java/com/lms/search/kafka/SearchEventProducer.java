package com.lms.search.kafka;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
public class SearchEventProducer {

    @Value("${topics.search}")
    private String topic;

    private final KafkaTemplate<String, String> kafkaTemplate;

    public SearchEventProducer(KafkaTemplate<String, String> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void sendEvent(String msg) {
        kafkaTemplate.send(topic, msg);
        System.out.println("📤 [SEARCH] Event sent → " + msg);
    }
}
