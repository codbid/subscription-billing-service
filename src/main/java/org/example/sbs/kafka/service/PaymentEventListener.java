package org.example.sbs.kafka.service;

import lombok.RequiredArgsConstructor;
import org.example.sbs.enums.InvoiceStatus;
import org.example.sbs.enums.PaymentStatus;
import org.example.sbs.kafka.events.InvoiceStatusUpdateEvent;
import org.example.sbs.kafka.events.PaymentEvent;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PaymentEventListener {
    private final KafkaTemplate<String, Object> kafkaTemplate;

    @KafkaListener(topics = "payment-events", groupId = "default", containerFactory = "kafkaListenerContainerFactory")
    public void listenPaymentEvent(PaymentEvent paymentEvent) {
        if(paymentEvent.getStatus() == PaymentStatus.SUBMITTED) {
            kafkaTemplate.send("invoice-status-update-events", new InvoiceStatusUpdateEvent(paymentEvent.getInvoiceId(), InvoiceStatus.PAYED));
        }
    }
}

