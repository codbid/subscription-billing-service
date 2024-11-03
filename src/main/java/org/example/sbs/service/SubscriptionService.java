package org.example.sbs.service;

import lombok.RequiredArgsConstructor;
import org.example.sbs.dto.request.AddRoleRequest;
import org.example.sbs.dto.request.CreateSubscriptionRequest;
import org.example.sbs.dto.response.CreateSubscriptionResponse;
import org.example.sbs.enums.SubscriptionStatus;
import org.example.sbs.exception.ForbiddenException;
import org.example.sbs.exception.NotFoundException;
import org.example.sbs.exception.enums.ExceptionMessage;
import org.example.sbs.kafka.events.SubscriptionCreatedEvent;
import org.example.sbs.kafka.events.SubscriptionStatusUpdateEvent;
import org.example.sbs.kafka.service.SubscriptionEventProducer;
import org.example.sbs.mapper.SubscriptionMapper;
import org.example.sbs.model.Subscription;
import org.example.sbs.multitenancy.TenantContext;
import org.example.sbs.repository.SubscriptionRepository;
import org.example.sbs.repository.UserRepository;
import org.example.sbs.security.UserDetailsImpl;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class SubscriptionService {
    private final SubscriptionRepository subscriptionRepository;
    private final SubscriptionMapper subscriptionMapper;
    private final SubscriptionEventProducer subscriptionEventProducer;
    private final UserRepository userRepository;
    private final UserService userService;

    public CreateSubscriptionResponse createSubscription(CreateSubscriptionRequest request, UserDetailsImpl userDetails) {
        if(userDetails.getTenantId() != null)
            throw new ForbiddenException(ExceptionMessage.ALREADY_HAVE_SUBSCRIPTION.getMessage());

        Subscription subscription = subscriptionMapper.toSubscription(request, userDetails.getId());
        subscription.setStatus(SubscriptionStatus.INACTIVE);

        subscription = subscriptionRepository.save(subscription);

        TenantContext.setCurrentTenant(subscription.getId());

        userService.addRole(userDetails.getId(), new AddRoleRequest("OWNER"));
        userService.addRole(userDetails.getId(), new AddRoleRequest("ADMIN"));

        subscriptionEventProducer.sendCreatedEvent(new SubscriptionCreatedEvent(subscription.getId()));

        return subscriptionMapper.toCreateSubscriptionResponse(subscription);
    }

    public CreateSubscriptionResponse getSubscriptionById(Long id) {
        if(!Objects.equals(id, TenantContext.getCurrentTenantId()))
            throw new ForbiddenException(ExceptionMessage.FORBIDDEN.getMessage());

        Subscription subscription = subscriptionRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(ExceptionMessage.ENTITY_NOT_FOUND.generateNotFoundEntityMessage("Subscription", id)));

        return subscriptionMapper.toCreateSubscriptionResponse(subscription);
    }

    public List<CreateSubscriptionResponse> getAllSubscriptions() {
        return subscriptionRepository.findAll().stream()
                .map(subscriptionMapper::toCreateSubscriptionResponse)
                .toList();
    }

    public CreateSubscriptionResponse updateSubscription(Long id, CreateSubscriptionRequest request) {
        if(!Objects.equals(id, TenantContext.getCurrentTenantId()))
            throw new ForbiddenException(ExceptionMessage.FORBIDDEN.getMessage());

        Subscription subscription = subscriptionRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(ExceptionMessage.ENTITY_NOT_FOUND.generateNotFoundEntityMessage("Subscription", id)));

        Subscription newSubscription = subscriptionMapper.toSubscription(request, subscription.getOwner().getId());
        newSubscription.setId(subscription.getId());
        return subscriptionMapper.toCreateSubscriptionResponse(subscriptionRepository.save(newSubscription));
    }

    public void deleteSubscription(Long id) {
        if(!Objects.equals(id, TenantContext.getCurrentTenantId()))
            throw new ForbiddenException(ExceptionMessage.FORBIDDEN.getMessage());

        Subscription subscription = subscriptionRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(ExceptionMessage.ENTITY_NOT_FOUND.generateNotFoundEntityMessage("Subscription", id)));

        subscription.getUsers().forEach( user ->
                userService.removeRole(user.getId(), new AddRoleRequest("ADMIN"))
        );

        userService.removeRole(subscription.getOwner().getId(), new AddRoleRequest("OWNER"));

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
        if(!Objects.equals(id, TenantContext.getCurrentTenantId()))
            throw new ForbiddenException(ExceptionMessage.FORBIDDEN.getMessage());

        subscriptionEventProducer.sendStatusUpdateEvent(new SubscriptionStatusUpdateEvent(id, SubscriptionStatus.PAUSED));
    }

    public void resumeSubscription(Long id) {
        subscriptionEventProducer.sendStatusUpdateEvent(new SubscriptionStatusUpdateEvent(id, SubscriptionStatus.ACTIVE));
    }
}
