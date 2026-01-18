package com.lms.notification.consumer;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class PaymentEventConsumer {

    @KafkaListener(
        topics = "${topics.payment}",
        groupId = "notification-group"
    )
    public void handlePaymentEvent(String message) {

        System.out.println("💰 [PAYMENT EVENT RECEIVED] -> " + message);

        if (message.startsWith("RefundRequested")) {
            handleRefundRequested(message);
        }
        else if (message.startsWith("RefundCompleted")) {
            handleRefundCompleted(message);
        }
        else {
            System.out.println("⚠️ Unknown payment event: " + message);
        }
    }

    private void handleRefundRequested(String message) {
        // Example:
        // RefundRequested|refundId:1|paymentId:10|amount:499

        System.out.println("📧 Sending refund requested notification...");
        System.out.println("📄 Details: " + message);

        // later:
        // emailService.sendRefundRequestedEmail(...)
    }

    private void handleRefundCompleted(String message) {
        // Example:
        // RefundCompleted|refundId:1

        System.out.println("✅ Sending refund completed notification...");
        System.out.println("📄 Details: " + message);

        // later:
        // emailService.sendRefundCompletedEmail(...)
    }
}
