package org.example.sbs.kafka.service;

import lombok.RequiredArgsConstructor;
import org.example.sbs.kafka.events.SubscriptionActivatedEvent;
import org.example.sbs.kafka.events.SubscriptionCreatedEvent;
import org.example.sbs.kafka.events.SubscriptionPausedEvent;
import org.example.sbs.kafka.events.SubscriptionStatusUpdateEvent;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SubscriptionEventProducer {
    private final KafkaTemplate<String, Object> kafkaTemplate;

    public void sendStatusUpdateEvent(SubscriptionStatusUpdateEvent event) {
        kafkaTemplate.send("subscription-status-update-events", event);
    }

    public void sendCreatedEvent(SubscriptionCreatedEvent event) {
        kafkaTemplate.send("subscription-created-events", event);
    }

    public void sendPausedEvent(SubscriptionPausedEvent event) {
        kafkaTemplate.send("subscription-paused-events", event);
    }

    public void sendActivatedEvent(SubscriptionActivatedEvent event) {
        kafkaTemplate.send("subscription-activated-events", event);
    }
}
