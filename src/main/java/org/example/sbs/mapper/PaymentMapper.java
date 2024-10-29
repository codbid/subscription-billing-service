package org.example.sbs.mapper;

import lombok.RequiredArgsConstructor;
import org.example.sbs.dto.request.CreatePaymentRequest;
import org.example.sbs.dto.response.CreatePaymentResponse;
import org.example.sbs.enums.PaymentMethod;
import org.example.sbs.enums.PaymentStatus;
import org.example.sbs.model.Invoice;
import org.example.sbs.model.Payment;
import org.example.sbs.repository.InvoiceRepository;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class PaymentMapper {
    private final InvoiceRepository invoiceRepository;
    private final InvoiceMapper invoiceMapper;

    public Payment toPayment(CreatePaymentRequest request) {
        Payment payment = new Payment();
        Invoice invoice = invoiceRepository.findById(request.getInvoiceId())
                .orElseThrow(() -> new RuntimeException("Invoice with id: " + request.getInvoiceId() + " not found"));
        payment.setInvoice(invoice);
        payment.setPaymentMethod(PaymentMethod.valueOf(request.getPaymentMethod()));
        payment.setStatus(PaymentStatus.valueOf(request.getStatus()));
        return payment;
    }

    public CreatePaymentResponse toCreatePaymentResponse(Payment payment) {
        CreatePaymentResponse response = new CreatePaymentResponse();
        response.setId(payment.getId());
        response.setInvoice(invoiceMapper.toCreateInvoiceResponse(payment.getInvoice()));
        response.setPaymentMethod(payment.getPaymentMethod().name());
        response.setStatus(payment.getStatus().name());
        response.setCreatedAt(payment.getCreatedAt());
        return response;
    }
}
