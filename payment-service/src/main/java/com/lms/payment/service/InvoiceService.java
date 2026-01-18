package com.lms.payment.service;

import com.lms.payment.model.Invoice;
import com.lms.payment.model.Payment;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.time.Instant;
import java.util.UUID;

@Service
public class InvoiceService {

    private final String invoicesDir = "invoices"; // relative directory; ensure existence

    public InvoiceService() {
        File d = new File(invoicesDir);
        if (!d.exists()) {
            d.mkdirs();
        }
    }

    public Invoice generateInvoiceForPayment(Payment payment) {

        Invoice inv = new Invoice();
        inv.setPaymentId(payment.getId());
        inv.setCreatedAt(Instant.now());

        String invoiceNumber =
                "INV-" + UUID.randomUUID().toString()
                        .substring(0, 8)
                        .toUpperCase();

        inv.setInvoiceNumber(invoiceNumber);

        // create a simple PDF
        String filename = invoicesDir + "/invoice-" + invoiceNumber + ".pdf";

        try (PDDocument doc = new PDDocument()) {

            PDPage page = new PDPage();
            doc.addPage(page);

            try (PDPageContentStream cs =
                         new PDPageContentStream(doc, page)) {

                cs.beginText();
                cs.setFont(PDType1Font.HELVETICA_BOLD, 16);
                cs.newLineAtOffset(50, 700);
                cs.showText("Invoice: " + invoiceNumber);

                cs.newLineAtOffset(0, -30);
                cs.setFont(PDType1Font.HELVETICA, 12);
                cs.showText("Payment ID: " + payment.getId());

                cs.newLineAtOffset(0, -20);
                cs.showText("User ID: " + payment.getUserId());

                cs.newLineAtOffset(0, -20);
                cs.showText("Course ID: " + payment.getCourseId());

                cs.newLineAtOffset(0, -20);
                cs.showText("Amount: " + payment.getAmount() + " " + payment.getCurrency());

                cs.newLineAtOffset(0, -40);
                cs.showText("Date: " + payment.getCreatedAt().toString());

                cs.endText();
            }

            doc.save(filename);

        } catch (IOException e) {
            throw new RuntimeException("Failed to create invoice PDF", e);
        }

        inv.setFilePath(filename);
        return inv;
    }
}
