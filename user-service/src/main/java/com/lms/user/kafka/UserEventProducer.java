package com.lms.user.kafka;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.lms.user.dto.UserEvent;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class UserEventProducer {

    private final KafkaTemplate<String, String> kafkaTemplate;
    private final ObjectMapper mapper = new ObjectMapper();
    private static final String TOPIC = "user-events";

    public UserEventProducer(KafkaTemplate<String, String> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void send(UserEvent event) {
        try {
            String json = mapper.writeValueAsString(event);
            kafkaTemplate.send(TOPIC, json);
            System.out.println("📤 USER EVENT SENT → " + json);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
