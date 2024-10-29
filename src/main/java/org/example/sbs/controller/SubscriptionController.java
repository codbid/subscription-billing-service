package org.example.sbs.controller;

import lombok.RequiredArgsConstructor;
import org.example.sbs.dto.request.CreateSubscriptionRequest;
import org.example.sbs.dto.response.CreateSubscriptionResponse;
import org.example.sbs.service.SubscriptionService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/subscriptions")
public class SubscriptionController {
    private final SubscriptionService subscriptionService;

    @PostMapping
    public ResponseEntity<CreateSubscriptionResponse> createSubscription(@RequestBody CreateSubscriptionRequest request) {
        return ResponseEntity.ok(subscriptionService.createSubscription(request));
    }

    @GetMapping("/{id}")
    public ResponseEntity<CreateSubscriptionResponse> getSubscriptionById(@PathVariable Long id) {
        return ResponseEntity.ok(subscriptionService.getSubscriptionById(id));
    }

    @GetMapping
    public ResponseEntity<List<CreateSubscriptionResponse>> getAllSubscriptions() {
        return ResponseEntity.ok(subscriptionService.getAllSubscriptions());
    }

    @PutMapping("/{id}")
    public ResponseEntity<CreateSubscriptionResponse> updateSubscription(@PathVariable Long id, @RequestBody CreateSubscriptionRequest request) {
        return ResponseEntity.ok(subscriptionService.updateSubscription(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSubscription(@PathVariable Long id) {
        subscriptionService.deleteSubscription(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/pause/{id}")
    public void pause(@PathVariable Long id) {
        subscriptionService.pauseSubscription(id);
    }

    @PostMapping("/activate/{id}")
    public void activate(@PathVariable Long id) {
        subscriptionService.resumeSubscription(id);
    }
}
