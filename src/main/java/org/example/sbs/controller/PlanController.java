package org.example.sbs.controller;

import lombok.RequiredArgsConstructor;
import org.example.sbs.dto.request.CreatePlanRequest;
import org.example.sbs.dto.response.CreatePlanResponse;
import org.example.sbs.service.PlanService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/plans")
public class PlanController {
    private final PlanService planService;

    @PostMapping
    public ResponseEntity<CreatePlanResponse> createPlan(CreatePlanRequest request) {
        return ResponseEntity.ok(planService.createPlan(request));
    }

    @GetMapping("/{id}")
    public ResponseEntity<CreatePlanResponse> getPlanById(@PathVariable Long id) {
        return ResponseEntity.ok(planService.getPlanById(id));
    }

    @GetMapping
    public ResponseEntity<List<CreatePlanResponse>> getAllPlans() {
        return ResponseEntity.ok(planService.getAllPlans());
    }

    @PutMapping("/{id}")
    public ResponseEntity<CreatePlanResponse> updatePlan(@PathVariable Long id, CreatePlanRequest request) {
        return ResponseEntity.ok(planService.updatePlan(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePlan(@PathVariable Long id) {
        planService.deletePlan(id);
        return ResponseEntity.noContent().build();
    }
}
