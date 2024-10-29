package org.example.sbs.kafka.service;

import lombok.RequiredArgsConstructor;
import org.example.sbs.enums.InvoiceStatus;
import org.example.sbs.enums.SubscriptionStatus;
import org.example.sbs.kafka.events.InvoiceCreateEvent;
import org.example.sbs.kafka.events.InvoiceStatusUpdateEvent;
import org.example.sbs.kafka.events.SubscriptionStatusUpdateEvent;
import org.example.sbs.model.Invoice;
import org.example.sbs.model.Subscription;
import org.example.sbs.repository.InvoiceRepository;
import org.example.sbs.repository.SubscriptionRepository;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
@RequiredArgsConstructor
public class InvoiceEventListener {
    private final KafkaTemplate<String, Object> kafkaTemplate;
    private final InvoiceRepository invoiceRepository;
    private final SubscriptionRepository subscriptionRepository;

    @KafkaListener(topics = "invoice-status-update-events", groupId = "default", containerFactory = "kafkaListenerContainerFactory")
    public void listenInvoiceStatusUpdateEvent(InvoiceStatusUpdateEvent event) {
        Invoice invoice = invoiceRepository.findById(event.getInvoiceId())
                .orElseThrow(() -> new RuntimeException("Invoice not found"));
        invoice.setStatus(event.getStatus());
        invoiceRepository.save(invoice);
        if(event.getStatus() == InvoiceStatus.PAYED) {
            kafkaTemplate.send("subscription-status-update-events", new SubscriptionStatusUpdateEvent(invoice.getSubscription().getId(), SubscriptionStatus.ACTIVE));
        }
    }

    @KafkaListener(topics = "invoice-create-events", groupId = "default", containerFactory = "kafkaListenerContainerFactory")
    public void listenInvoiceCreateEvent(InvoiceCreateEvent event) {
        Subscription subscription = subscriptionRepository.findById(event.getSubscriptionId())
                .orElseThrow(() -> new RuntimeException("Subscription not found"));
        Invoice invoice = new Invoice();
        invoice.setSubscription(subscription);
        switch (subscription.getPlan().getBillingCycle()) {
            case DAILY -> invoice.setDescription("Subscription: " + subscription.getPlan().getName() + " for 1 Day");
            case WEEKLY -> invoice.setDescription("Subscription: " + subscription.getPlan().getName() + " for 1 Week");
            case MONTHLY -> invoice.setDescription("Subscription: " + subscription.getPlan().getName() + " for 1 Month");
            case YEARLY -> invoice.setDescription("Subscription: " + subscription.getPlan().getName() + " for 1 Year");
        }
        invoice.setAmount(subscription.getPlan().getPrice());
        invoice.setStatus(InvoiceStatus.AWAITING_PAYMENT);
        invoice.setCreatedDate(LocalDate.now());
        invoiceRepository.save(invoice);
    }
}
