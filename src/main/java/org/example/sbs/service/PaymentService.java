package org.example.sbs.service;

import lombok.RequiredArgsConstructor;
import org.example.sbs.dto.request.CreatePaymentRequest;
import org.example.sbs.dto.response.CreatePaymentResponse;
import org.example.sbs.enums.PaymentStatus;
import org.example.sbs.kafka.events.PaymentEvent;
import org.example.sbs.kafka.service.PaymentEventProducer;
import org.example.sbs.mapper.PaymentMapper;
import org.example.sbs.model.Payment;
import org.example.sbs.repository.PaymentRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PaymentService {
    private final PaymentRepository paymentRepository;
    private final PaymentMapper paymentMapper;
    private final PaymentEventProducer paymentEventProducer;

    public CreatePaymentResponse createPayment(CreatePaymentRequest request) {
        Payment payment = paymentMapper.toPayment(request);
        payment.setCreatedAt(LocalDateTime.now());
        paymentEventProducer.sendPayment(new PaymentEvent(request.getInvoiceId(), PaymentStatus.valueOf(request.getStatus())));
        return paymentMapper.toCreatePaymentResponse(paymentRepository.save(payment));
    }

    public CreatePaymentResponse getPaymentById(Long id) {
        Payment payment = paymentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Payment with id: " + id + " not found"));
        return paymentMapper.toCreatePaymentResponse(payment);
    }

    public List<CreatePaymentResponse> getAllPayments() {
        return paymentRepository.findAll().stream()
                .map(paymentMapper::toCreatePaymentResponse)
                .toList();
    }
//
//    public CreatePaymentResponse updatePayment(Long id, CreatePaymentRequest request) {
//        Payment payment = paymentRepository.findById(id)
//                .orElseThrow(() -> new RuntimeException("Payment with id: " + id + " not found"));
//        Payment newPayment = paymentMapper.toPayment(request);
//        newPayment.setId(id);
//        return paymentMapper.toCreatePaymentResponse(paymentRepository.save(newPayment));
//    }
//
//    public void deletePayment(Long id) {
//        paymentRepository.deleteById(id);
//    }
}
