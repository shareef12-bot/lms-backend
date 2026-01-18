package com.lms.payment.controller;

import com.lms.payment.model.Refund;
import com.lms.payment.service.RefundService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/payments")
public class RefundController {

    private final RefundService refundService;

    public RefundController(RefundService refundService) {
        this.refundService = refundService;
    }

    // 🔁 REQUEST REFUND
    @PostMapping("/{paymentId}/refund")
    public ResponseEntity<Refund> refundPayment(
            @PathVariable Long paymentId,
            @RequestBody Map<String, Object> body) {

        Double amount = Double.valueOf(body.get("amount").toString());
        String reason = body.get("reason").toString();

        return ResponseEntity.ok(
                refundService.requestRefund(paymentId, amount, reason)
        );
    }
}
