package org.example.sbs.service;

import lombok.RequiredArgsConstructor;
import org.example.sbs.dto.request.CreatePaymentRequest;
import org.example.sbs.dto.response.CreatePaymentResponse;
import org.example.sbs.enums.PaymentStatus;
import org.example.sbs.enums.SubscriptionStatus;
import org.example.sbs.kafka.events.PaymentEvent;
import org.example.sbs.kafka.events.SubscriptionStatusUpdateEvent;
import org.example.sbs.kafka.service.PaymentEventProducer;
import org.example.sbs.mapper.PaymentMapper;
import org.example.sbs.model.Payment;
import org.example.sbs.repository.PaymentRepository;
import org.example.sbs.yookassa.YooKassaService;
import org.example.sbs.yookassa.model.PaymentRequest;
import org.example.sbs.yookassa.model.PaymentResponse;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PaymentService {
    private final PaymentRepository paymentRepository;
    private final PaymentMapper paymentMapper;
    private final PaymentEventProducer paymentEventProducer;
    private final YooKassaService yooKassaService;

    public CreatePaymentResponse createPayment(CreatePaymentRequest request) {
        Payment payment = paymentMapper.toPayment(request);
        payment.setStatus(PaymentStatus.PENDING);
        payment.setIdempotenceKey(UUID.randomUUID().toString());
        payment = yooKassaService.createPayment(payment, "127.0.0.1:8080/swagger-ui/index.html");  // изменить url
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

    @Scheduled(fixedRate = 10000)
    public void checkPayments() {
        paymentRepository.findAllByStatus(PaymentStatus.PENDING)
                .ifPresent(payments -> payments.forEach(payment -> {
                    PaymentResponse response = yooKassaService.getPayment(payment);
                    if(!response.getStatus().equalsIgnoreCase(PaymentStatus.PENDING.name())) {
                        payment.setStatus(PaymentStatus.valueOf(response.getStatus().toUpperCase()));
                        paymentRepository.save(payment);
                        paymentEventProducer.sendPayment(new PaymentEvent(payment.getInvoice().getId(), payment.getStatus()));
                    }
                }));
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
