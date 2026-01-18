package com.lms.payment.kafka;

import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class PaymentEventProducer {

    private final KafkaTemplate<String, String> kafkaTemplate;

    // hard-coded here, NOT in services
    private static final String PAYMENT_TOPIC = "payment-events";
    private static final String INVOICE_TOPIC = "invoice-events";

    public PaymentEventProducer(KafkaTemplate<String, String> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void publishPaymentEvent(String payload) {
        kafkaTemplate.send(PAYMENT_TOPIC, payload);
        System.out.println("📤 Payment event sent: " + payload);
    }

    public void publishInvoiceEvent(String payload) {
        kafkaTemplate.send(INVOICE_TOPIC, payload);
        System.out.println("📤 Invoice event sent: " + payload);
    }
}  
