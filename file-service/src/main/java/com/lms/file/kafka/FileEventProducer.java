package com.lms.file.kafka;

import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
public class FileEventProducer {

    private final KafkaTemplate<String, String> kafkaTemplate;

    public FileEventProducer(KafkaTemplate<String, String> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void sendFileUploadedEvent(Long fileId) {
        kafkaTemplate.send("file-uploaded", "FILE_UPLOADED:" + fileId);
    }
}
