package com.lms.payment.service;

import com.lms.payment.kafka.PaymentEventProducer;
import com.lms.payment.model.Payment;
import com.lms.payment.model.Refund;
import com.lms.payment.repository.PaymentRepository;
import com.lms.payment.repository.RefundRepository;
import org.springframework.stereotype.Service;

import java.time.Instant;

@Service
public class RefundService {

    private final RefundRepository refundRepository;
    private final PaymentRepository paymentRepository;
    private final PaymentEventProducer producer;

    public RefundService(RefundRepository refundRepository,
                         PaymentRepository paymentRepository,
                         PaymentEventProducer producer) {
        this.refundRepository = refundRepository;
        this.paymentRepository = paymentRepository;
        this.producer = producer;
    }

    public Refund requestRefund(Long paymentId, Double amount, String reason) {

        Payment payment = paymentRepository.findById(paymentId)
                .orElseThrow(() -> new RuntimeException("Payment not found"));

        Refund refund = new Refund();
        refund.setPaymentId(paymentId);
        refund.setAmount(amount);
        refund.setReason(reason);
        refund.setRequestedAt(Instant.now());
        refund.setStatus("REQUESTED");

        Refund savedRefund = refundRepository.save(refund);

        // ✅ Mark payment as REFUND_PROCESSING
        payment.setStatus("REFUND_PROCESSING");
        paymentRepository.save(payment);

        // ✅ Publish refund requested event
        producer.publishPaymentEvent(
                "RefundRequested|refundId:" + savedRefund.getId()
                        + "|paymentId:" + paymentId
                        + "|amount:" + amount
        );

        // ✅ Simulate refund success
        savedRefund.setStatus("COMPLETED");
        savedRefund.setCompletedAt(Instant.now());
        refundRepository.save(savedRefund);

        payment.setStatus("REFUNDED");
        paymentRepository.save(payment);

        // ✅ Publish refund completed event
        producer.publishPaymentEvent(
                "RefundCompleted|refundId:" + savedRefund.getId()
        );

        return savedRefund;
    }
}
