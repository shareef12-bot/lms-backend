package com.lms.payment.dto;

public class PaymentResponse {

    private Long paymentId;
    private String status; 
    private String message;
    private Double amount;

    public PaymentResponse() {}

    public PaymentResponse(Long paymentId, String status, String message, Double amount) {
        this.paymentId = paymentId;
        this.status = status;
        this.message = message;
        this.amount = amount;
    }

    public Long getPaymentId() {
        return paymentId;
    }

    public void setPaymentId(Long paymentId) {
        this.paymentId = paymentId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }
}
