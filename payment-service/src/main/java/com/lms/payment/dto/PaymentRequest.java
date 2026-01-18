package com.lms.payment.dto;

import jakarta.validation.constraints.NotNull;

public class PaymentRequest {

    @NotNull
    private Long userId;

    @NotNull
    private Long courseId;

    @NotNull
    private Double amount;

    private String paymentMethod;  // UPI / CARD / NETBANKING

    public PaymentRequest() {}

    public PaymentRequest(Long userId, Long courseId, Double amount, String paymentMethod) {
        this.userId = userId;
        this.courseId = courseId;
        this.amount = amount;
        this.paymentMethod = paymentMethod;
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

    public String getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
    }
}
