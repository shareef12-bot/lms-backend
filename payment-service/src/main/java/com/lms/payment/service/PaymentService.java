package com.lms.payment.service;

import com.lms.payment.kafka.AnalyticsProducer;
import com.lms.payment.kafka.PaymentEventProducer;
import com.lms.payment.model.Invoice;
import com.lms.payment.model.Payment;
import com.lms.payment.repository.InvoiceRepository;
import com.lms.payment.repository.PaymentRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Optional;

@Service
public class PaymentService {

    private final PaymentRepository paymentRepository;
    private final PaymentEventProducer paymentEventProducer;
    private final InvoiceService invoiceService;
    private final AnalyticsProducer analyticsProducer;
    private final InvoiceRepository invoiceRepository;

    public PaymentService(PaymentRepository paymentRepository,
                          PaymentEventProducer paymentEventProducer,
                          InvoiceService invoiceService,
                          AnalyticsProducer analyticsProducer,
                          InvoiceRepository invoiceRepository) {
        this.paymentRepository = paymentRepository;
        this.paymentEventProducer = paymentEventProducer;
        this.invoiceService = invoiceService;
        this.analyticsProducer = analyticsProducer;
        this.invoiceRepository = invoiceRepository;
    }

    public Payment createPayment(Payment p) {
        p.setStatus("SUCCESS");
        p.setCreatedAt(Instant.now());
        p.setUpdatedAt(Instant.now());

        Payment saved = paymentRepository.save(p);

        // ✅ Publish payment event (topic handled by producer)
        String paymentPayload =
                "PaymentCreated|paymentId:" + saved.getId()
                        + "|userId:" + saved.getUserId()
                        + "|courseId:" + saved.getCourseId()
                        + "|amount:" + saved.getAmount();

        paymentEventProducer.publishPaymentEvent(paymentPayload);

        // ✅ Send analytics event
        analyticsProducer.sendAnalytics(
                "analytics-events",
                "payment_created:" + saved.getId()
        );

        // ✅ Generate invoice
        Invoice invoice = invoiceService.generateInvoiceForPayment(saved);
        invoiceRepository.save(invoice);

        // ✅ Publish invoice event
        String invoicePayload =
                "InvoiceCreated|paymentId:" + saved.getId()
                        + "|invoiceNumber:" + invoice.getInvoiceNumber();

        paymentEventProducer.publishInvoiceEvent(invoicePayload);

        return saved;
    }

    public Page<Payment> listPaymentsForUser(Long userId, Pageable pageable) {
        return paymentRepository.findByUserId(userId, pageable);
    }

    public Optional<Payment> getById(Long id) {
        return paymentRepository.findById(id);
    }

    public Payment updateStatus(Long id, String status) {
        Payment p = paymentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Payment not found"));

        p.setStatus(status);
        p.setUpdatedAt(Instant.now());
        return paymentRepository.save(p);
    }
}
