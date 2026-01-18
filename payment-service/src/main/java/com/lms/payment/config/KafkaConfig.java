package com.lms.payment.config;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class KafkaConfig {

    @Bean
    public NewTopic paymentTopic() {
        return new NewTopic("payment-events", 1, (short)1);
    }

    @Bean
    public NewTopic analyticsTopic() {
        return new NewTopic("analytics-events", 1, (short)1);
    }

    @Bean
    public NewTopic invoiceTopic() {
        return new NewTopic("invoice-events", 1, (short)1);
    }
}
