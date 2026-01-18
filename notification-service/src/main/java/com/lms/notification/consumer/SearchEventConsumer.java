package com.lms.notification.consumer;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class SearchEventConsumer {

    @KafkaListener(topics = "search-events", groupId = "notification-group")
    public void handleSearchEvent(String message) {
        System.out.println("🔍 [NOTIFICATION] SEARCH EVENT RECEIVED → " + message);
    }
}
