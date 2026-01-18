package com.lms.payment.model;

import jakarta.persistence.*;
import java.time.Instant;

@Entity
@Table(name = "refunds")
public class Refund {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long paymentId;
    private Double amount;
    private String status; // REQUESTED, PROCESSING, COMPLETED, FAILED
    private Instant requestedAt;
    private Instant completedAt;
    private String reason;

    public Refund() {}
    // getters & setters...
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Long getPaymentId() { return paymentId; }
    public void setPaymentId(Long paymentId) { this.paymentId = paymentId; }
    public Double getAmount() { return amount; }
    public void setAmount(Double amount) { this.amount = amount; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public Instant getRequestedAt() { return requestedAt; }
    public void setRequestedAt(Instant requestedAt) { this.requestedAt = requestedAt; }
    public Instant getCompletedAt() { return completedAt; }
    public void setCompletedAt(Instant completedAt) { this.completedAt = completedAt; }
    public String getReason() { return reason; }
    public void setReason(String reason) { this.reason = reason; }
}
