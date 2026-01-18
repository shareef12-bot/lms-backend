//package com.lms.student.kafka;
//
//import com.fasterxml.jackson.core.JsonProcessingException;
//import com.fasterxml.jackson.databind.ObjectMapper;
//import org.springframework.kafka.core.KafkaTemplate;
//import org.springframework.stereotype.Service;
//
//import java.util.Map;
//
//@Service
//public class StudentEventProducer {
//
//    private final KafkaTemplate<String, String> kafkaTemplate;
//    private final ObjectMapper objectMapper;
//
//    private static final String TOPIC = "student-events";
//
//    public StudentEventProducer(KafkaTemplate<String, String> kafkaTemplate,
//                                ObjectMapper objectMapper) {
//        this.kafkaTemplate = kafkaTemplate;
//        this.objectMapper = objectMapper;
//    }
//
//    public void send(String eventType, Map<String, Object> payload) {
//        try {
//            Map<String, Object> event = Map.of(
//                    "eventType", eventType,
//                    "payload", payload
//            );
//
//            String message = objectMapper.writeValueAsString(event);
//
//            kafkaTemplate.send(TOPIC, message);
//
//            System.out.println("📨 Student event sent: " + message);
//
//        } catch (JsonProcessingException e) {
//            throw new RuntimeException("Failed to serialize student event", e);
//        }
//    }
//}

package com.lms.student.kafka;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class StudentEventProducer {

    private final KafkaTemplate<String, String> kafkaTemplate;
    private final ObjectMapper objectMapper;

    @Value("${topics.student}")
    private String studentTopic;

    public StudentEventProducer(
            KafkaTemplate<String, String> kafkaTemplate,
            ObjectMapper objectMapper
    ) {
        this.kafkaTemplate = kafkaTemplate;
        this.objectMapper = objectMapper;
    }

    public void send(String type, Map<String, Object> payload) {
        try {
            String message = objectMapper.writeValueAsString(
                Map.of(
                    "type", type,
                    "payload", payload
                )
            );

            kafkaTemplate.send(studentTopic, message);

            System.out.println("📤 STUDENT EVENT SENT: " + message);

        } catch (JsonProcessingException e) {
            throw new RuntimeException("Failed to serialize student event", e);
        }
    }
}

