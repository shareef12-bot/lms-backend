package com.lms.payment.repository;

import com.lms.payment.model.Invoice;
import org.springframework.data.jpa.repository.JpaRepository;

public interface InvoiceRepository extends JpaRepository<Invoice, Long> {
    Invoice findByInvoiceNumber(String invoiceNumber);
}
