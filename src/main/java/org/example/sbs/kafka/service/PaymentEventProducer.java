package org.example.sbs.kafka.service;

import lombok.RequiredArgsConstructor;
import org.example.sbs.kafka.events.PaymentEvent;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PaymentEventProducer {
    private final KafkaTemplate<String, Object> kafkaTemplate;

    public void sendPayment(PaymentEvent event) {
        kafkaTemplate.send("payment-events", event);
    }
}
