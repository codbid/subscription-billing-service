package org.example.sbs.service;

import lombok.RequiredArgsConstructor;
import org.example.sbs.dto.request.CreateSubscriptionRequest;
import org.example.sbs.dto.response.CreateSubscriptionResponse;
import org.example.sbs.enums.SubscriptionStatus;
import org.example.sbs.kafka.events.SubscriptionCreatedEvent;
import org.example.sbs.kafka.events.SubscriptionStatusUpdateEvent;
import org.example.sbs.kafka.service.SubscriptionEventProducer;
import org.example.sbs.mapper.SubscriptionMapper;
import org.example.sbs.model.Subscription;
import org.example.sbs.repository.SubscriptionRepository;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class SubscriptionService {
    private final SubscriptionRepository subscriptionRepository;
    private final SubscriptionMapper subscriptionMapper;
    private final SubscriptionEventProducer subscriptionEventProducer;

    public CreateSubscriptionResponse createSubscription(CreateSubscriptionRequest request) {
        Subscription subscription = subscriptionMapper.toSubscription(request);
        subscription.setStatus(SubscriptionStatus.INACTIVE);
        subscription = subscriptionRepository.save(subscription);
        subscriptionEventProducer.sendCreatedEvent(new SubscriptionCreatedEvent(subscription.getId()));
        return subscriptionMapper.toCreateSubscriptionResponse(subscription);
    }

    public CreateSubscriptionResponse getSubscriptionById(Long id) {
        Subscription subscription = subscriptionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Subscription with id: " + id + " not found"));
        return subscriptionMapper.toCreateSubscriptionResponse(subscription);
    }

    public List<CreateSubscriptionResponse> getAllSubscriptions() {
        return subscriptionRepository.findAll().stream()
                .map(subscriptionMapper::toCreateSubscriptionResponse)
                .toList();
    }

    public CreateSubscriptionResponse updateSubscription(Long id, CreateSubscriptionRequest request) {
        Subscription subscription = subscriptionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Subscription with id: " + id + " not found"));
        Subscription newSubscription = subscriptionMapper.toSubscription(request);
        newSubscription.setId(subscription.getId());
        return subscriptionMapper.toCreateSubscriptionResponse(subscriptionRepository.save(newSubscription));
    }

    public void deleteSubscription(Long id) {
        Subscription subscription = subscriptionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Subscription with id: " + id + " not found"));
        subscriptionRepository.delete(subscription);
    }

    @Scheduled(cron = "0 0 0 * * *")
    public void checkSubscriptions() {
        subscriptionRepository.findAllByStatusAndEndDate(SubscriptionStatus.ACTIVE, LocalDate.now())
                .ifPresent(subscriptions -> subscriptions.forEach(subscription -> {
                    subscriptionEventProducer.sendStatusUpdateEvent(new SubscriptionStatusUpdateEvent(subscription.getId(), SubscriptionStatus.PAUSED));
                }));
        subscriptionRepository.findAllByStatus(SubscriptionStatus.PAUSED)
                .ifPresent(subscriptions -> subscriptions.forEach(subscription -> {
                    subscriptionEventProducer.sendStatusUpdateEvent(new SubscriptionStatusUpdateEvent(subscription.getId(), SubscriptionStatus.INACTIVE));
                }));
    }

    public void pauseSubscription(Long id) {
        subscriptionEventProducer.sendStatusUpdateEvent(new SubscriptionStatusUpdateEvent(id, SubscriptionStatus.PAUSED));
    }

    public void resumeSubscription(Long id) {
        subscriptionEventProducer.sendStatusUpdateEvent(new SubscriptionStatusUpdateEvent(id, SubscriptionStatus.ACTIVE));
    }
}
