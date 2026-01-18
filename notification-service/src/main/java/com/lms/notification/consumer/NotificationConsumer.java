package com.lms.notification.consumer;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.lms.notification.dto.NotificationEvent;
import com.lms.notification.service.EmailSenderService;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class NotificationConsumer {

    private final ObjectMapper objectMapper = new ObjectMapper();
    private final EmailSenderService emailSenderService;

    public NotificationConsumer(EmailSenderService emailSenderService) {
        this.emailSenderService = emailSenderService;
    }

    @KafkaListener(topics = "video-uploaded", groupId = "notification-group")
    public void consumeMessage(String message) throws Exception {

        NotificationEvent event =
                objectMapper.readValue(message, NotificationEvent.class);

        System.out.println("==== Notification Service ====");
        System.out.println("Type: " + event.getType());
        System.out.println("Payload: " + event.getPayload());
        System.out.println("==============================");

        // send email internally
        if (event.getType().equals("VIDEO_UPLOADED")) {
            emailSenderService.sendEmail(
                "admin@lms.com",
                "New Video Uploaded",
                "A new video was uploaded: " + event.getPayload().get("fileName")
            );
        }
    }
}
