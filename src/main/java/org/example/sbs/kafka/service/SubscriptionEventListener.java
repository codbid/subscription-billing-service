package org.example.sbs.kafka.service;

import lombok.RequiredArgsConstructor;
import org.example.sbs.enums.SubscriptionStatus;
import org.example.sbs.kafka.events.*;
import org.example.sbs.model.Subscription;
import org.example.sbs.repository.SubscriptionRepository;
import org.example.sbs.repository.UserRepository;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
@RequiredArgsConstructor
public class SubscriptionEventListener {
    private final KafkaTemplate<String, Object> kafkaTemplate;
    private final SubscriptionRepository subscriptionRepository;
    private final UserRepository userRepository;

    @KafkaListener(topics = "subscription-status-update-events", groupId = "default", containerFactory = "kafkaListenerContainerFactory")
    public void listenStatusUpdateEvent(SubscriptionStatusUpdateEvent event) {
        Subscription subscription = subscriptionRepository.findById(event.getSubscriptionId())
                .orElseThrow(() -> new RuntimeException("Subscription not found"));
        if(event.getStatus() == SubscriptionStatus.PAUSED)
            kafkaTemplate.send("subscription-paused-events", new SubscriptionPausedEvent(subscription.getId()));
        else if(event.getStatus() == SubscriptionStatus.ACTIVE)
            kafkaTemplate.send("subscription-activated-events", new SubscriptionActivatedEvent(subscription.getId()));
        subscription.setStatus(event.getStatus());
        subscriptionRepository.save(subscription);
    }

    @KafkaListener(topics = "subscription-created-events", groupId = "default", containerFactory = "kafkaListenerContainerFactory")
    public void listenCreatedEvent(SubscriptionCreatedEvent event) {
        kafkaTemplate.send("invoice-create-events", new InvoiceCreateEvent(event.getSubscriptionId()));

    }

    @KafkaListener(topics = "subscription-paused-events", groupId = "default", containerFactory = "kafkaListenerContainerFactory")
    public void listenPausedEvent(SubscriptionPausedEvent event) {
        kafkaTemplate.send("invoice-create-events", new InvoiceCreateEvent(event.getSubscriptionId()));
    }

    @KafkaListener(topics = "subscription-activated-events", groupId = "default", containerFactory = "kafkaListenerContainerFactory")
    public void listenActivatedEvent(SubscriptionActivatedEvent event) {
        Subscription subscription = subscriptionRepository.findById(event.getSubscriptionId())
                .orElseThrow(() -> new RuntimeException("Invoice not found"));
        subscription.setStartDate(LocalDate.now());
        switch (subscription.getPlan().getBillingCycle()) {
            case DAILY -> subscription.setEndDate(LocalDate.now().plusDays(1));
            case WEEKLY -> subscription.setEndDate(LocalDate.now().plusWeeks(1));
            case MONTHLY -> subscription.setEndDate(LocalDate.now().plusMonths(1));
            case YEARLY -> subscription.setEndDate(LocalDate.now().plusYears(1));
        }
        subscriptionRepository.save(subscription);
    }
}
