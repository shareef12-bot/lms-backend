package com.lms.notification.dto;

import java.util.Map;

public class NotificationEvent {

    private String type;
    private Map<String, Object> payload;

    public NotificationEvent() {}

    public NotificationEvent(String type, Map<String, Object> payload) {
        this.type = type;
        this.payload = payload;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Map<String, Object> getPayload() {
        return payload;
    }

    public void setPayload(Map<String, Object> payload) {
        this.payload = payload;
    }

    @Override
    public String toString() {
        return "NotificationEvent{" +
                "type='" + type + '\'' +
                ", payload=" + payload +
                '}';
    }
}
