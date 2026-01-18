package com.lms.assessment.kafka;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.lms.assessment.dto.AssessmentEvent;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class AssessmentEventProducer {

    private final KafkaTemplate<String, String> kafkaTemplate;
    private static final String TOPIC = "assessment-events";
    private final ObjectMapper mapper = new ObjectMapper();

    public AssessmentEventProducer(KafkaTemplate<String, String> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void sendEvent(AssessmentEvent event) {
        try {
            String json = mapper.writeValueAsString(event);
            kafkaTemplate.send(TOPIC, json);
            System.out.println("✔ Sent Assessment Kafka Event: " + json);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
