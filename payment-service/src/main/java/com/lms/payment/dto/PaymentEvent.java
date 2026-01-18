package com.lms.payment.dto;

public class PaymentEvent {

    private Long paymentId;
    private Long userId;
    private Long courseId;
    private Double amount;
    private String status; // SUCCESS / FAILED

    public PaymentEvent() {}

    public PaymentEvent(Long paymentId, Long userId, Long courseId, Double amount, String status) {
        this.paymentId = paymentId;
        this.userId = userId;
        this.courseId = courseId;
        this.amount = amount;
        this.status = status;
    }

    public Long getPaymentId() {
        return paymentId;
    }

    public void setPaymentId(Long paymentId) {
        this.paymentId = paymentId;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getCourseId() {
        return courseId;
    }

    public void setCourseId(Long courseId) {
        this.courseId = courseId;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
