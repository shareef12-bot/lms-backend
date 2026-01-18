package com.lms.student.kafka;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class UserEventConsumer {

    @KafkaListener(
        topics = "user-events",
        groupId = "student-service-group"
    )
    public void consume(Map<String, Object> event) {

        System.out.println("\n===== USER EVENT RECEIVED IN STUDENT SERVICE =====");
        System.out.println(event);
        System.out.println("=================================================\n");

        // Example expected payload:
        // {
        //   "type": "USER_CREATED",
        //   "userId": 12,
        //   "email": "test@gmail.com"
        // }

        if ("USER_CREATED".equals(event.get("type"))) {
            Long userId = Long.valueOf(event.get("userId").toString());
            System.out.println("Auto-create student for userId = " + userId);

            // 👉 Later you can call StudentService.create(userId)
        }
    }
}
