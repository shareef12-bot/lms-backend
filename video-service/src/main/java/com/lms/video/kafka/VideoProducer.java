package com.lms.video.kafka;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class VideoProducer {

    private final KafkaTemplate<String, String> kafkaTemplate;
    private final ObjectMapper mapper = new ObjectMapper();

    public VideoProducer(KafkaTemplate<String, String> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void sendVideoUploadedEvent(String fileName) {
        try {
            Map<String, Object> payload = new HashMap<>();
            payload.put("fileName", fileName);

            Map<String, Object> event = new HashMap<>();
            event.put("type", "VIDEO_UPLOADED");
            event.put("payload", payload);

            String json = mapper.writeValueAsString(event);

            kafkaTemplate.send("video-uploaded", json);

        } catch (Exception e) {
            throw new RuntimeException("Failed to send Kafka event");
        }
        
    }
    public void sendVideoDeletedEvent(String fileName) {
        try {
            kafkaTemplate.send("video-deleted", fileName);
        } catch (Exception e) {
            System.out.println("Failed to send video-deleted event");
        }
    }

}
