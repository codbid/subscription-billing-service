package org.example.sbs.mapper;

import lombok.RequiredArgsConstructor;
import org.example.sbs.dto.request.CreateSubscriptionRequest;
import org.example.sbs.dto.response.CreateSubscriptionResponse;
import org.example.sbs.model.Plan;
import org.example.sbs.model.Subscription;
import org.example.sbs.model.User;
import org.example.sbs.repository.PlanRepository;
import org.example.sbs.repository.UserRepository;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class SubscriptionMapper {
    private final UserRepository userRepository;
    private final PlanRepository planRepository;
    private final UserMapper userMapper;
    private final PlanMapper planMapper;

    public Subscription toSubscription(CreateSubscriptionRequest request) {
        Subscription subscription = new Subscription();
        User owner = userRepository.findById(request.getOwnerId())
                .orElseThrow(() -> new RuntimeException("User with id: " + request.getOwnerId() + " not found"));
        Plan plan = planRepository.findById(request.getPlanId())
                .orElseThrow(() -> new RuntimeException("Plan with id: " + request.getPlanId() + " not found"));
        subscription.setOwner(owner);
        subscription.setPlan(plan);
        return subscription;
    }

    public CreateSubscriptionResponse toCreateSubscriptionResponse(Subscription subscription) {
        CreateSubscriptionResponse response = new CreateSubscriptionResponse();
        response.setId(subscription.getId());
        response.setOwner(userMapper.toCreateUserResponse(subscription.getOwner()));
        response.setPlan(planMapper.toCreatePlanResponse(subscription.getPlan()));
        response.setStatus(subscription.getStatus().name());
        response.setStartDate(subscription.getStartDate());
        response.setEndDate(subscription.getEndDate());
        return response;
    }
}
