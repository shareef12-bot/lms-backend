////package com.lms.student.kafka;
////
////import com.fasterxml.jackson.databind.ObjectMapper;
////import org.springframework.beans.factory.annotation.Value;
////import org.springframework.kafka.core.KafkaTemplate;
////import org.springframework.stereotype.Component;
////
////import java.util.Map;
////
////@Component
////public class TrainerEventProducer {
////
////    private final KafkaTemplate<String, String> kafkaTemplate;
////    private final ObjectMapper objectMapper;
////
////    @Value("${topics.trainer:trainer-events}")
////    private String topic;
////
////    public TrainerEventProducer(KafkaTemplate<String, String> kafkaTemplate,
////                                ObjectMapper objectMapper) {
////        this.kafkaTemplate = kafkaTemplate;
////        this.objectMapper = objectMapper;
////    }
////
////    public void send(String eventType, Map<String, Object> payload) {
////
////        try {
////            payload.put("eventType", eventType);
////            String message = objectMapper.writeValueAsString(payload);
////
////            kafkaTemplate.send(topic, message)
////
////                    .whenComplete((result, ex) -> {
////                        if (ex != null) {
////                            System.err.println("❌ Kafka error (trainer): " + ex.getMessage());
////                        } else {
////                            System.out.println("✅ Trainer event sent: " + message);
////                        }
////                    });
////
////        } catch (Exception e) {
////            System.err.println("❌ Kafka serialization error: " + e.getMessage());
////        }
////    }
////
////}
//package com.lms.student.kafka;
//
//import com.fasterxml.jackson.databind.ObjectMapper;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.kafka.core.KafkaTemplate;
//import org.springframework.stereotype.Component;
//
//import java.util.Map;
//
//@Component
//public class TrainerEventProducer {
//
//    private final KafkaTemplate<String, String> kafkaTemplate;
//    private final ObjectMapper objectMapper;
//
//    @Value("${topics.trainer:trainer-events}")
//    private String topic;
//
//    public TrainerEventProducer(KafkaTemplate<String, String> kafkaTemplate,
//                                ObjectMapper objectMapper) {
//        this.kafkaTemplate = kafkaTemplate;
//        this.objectMapper = objectMapper;
//    }
//
//    public void send(String eventType, Map<String, Object> payload) {
//        try {
//            payload.put("eventType", eventType);
//            String message = objectMapper.writeValueAsString(payload);
//
//            kafkaTemplate.send(topic, message)
//                    .whenComplete((result, ex) -> {
//                        if (ex != null) {
//                            System.err.println("❌ Kafka send failed");
//                            ex.printStackTrace(); // 🔥 shows REAL error
//                        } else {
//                            System.out.println("✅ Trainer event sent → " + message);
//                        }
//                    });
//
//        } catch (Exception e) {
//            System.err.println("❌ Kafka serialization failed");
//            e.printStackTrace(); // 🔥 shows REAL error
//        }
//    }
//}
package com.lms.student.kafka;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class TrainerEventProducer {

    private final KafkaTemplate<String, String> kafkaTemplate;
    private final ObjectMapper objectMapper;

    @Value("${topics.trainer:trainer-events}")
    private String topic;

    public TrainerEventProducer(KafkaTemplate<String, String> kafkaTemplate,
                                ObjectMapper objectMapper) {
        this.kafkaTemplate = kafkaTemplate;
        this.objectMapper = objectMapper;
    }

    public void send(String eventType, Map<String, Object> payload) {

        try {
            // ✅ CREATE MUTABLE MAP
            Map<String, Object> event = new HashMap<>(payload);
            event.put("eventType", eventType);

            String message = objectMapper.writeValueAsString(event);

            kafkaTemplate.send(topic, message)
                    .whenComplete((result, ex) -> {
                        if (ex != null) {
                            System.err.println("❌ Kafka send failed");
                            ex.printStackTrace();
                        } else {
                            System.out.println("✅ Trainer event sent → " + message);
                        }
                    });

        } catch (Exception e) {
            System.err.println("❌ Kafka serialization failed");
            e.printStackTrace();
        }
    }
}

