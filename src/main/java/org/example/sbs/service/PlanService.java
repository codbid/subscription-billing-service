package org.example.sbs.service;

import lombok.RequiredArgsConstructor;
import org.example.sbs.dto.request.CreatePlanRequest;
import org.example.sbs.dto.response.CreatePlanResponse;
import org.example.sbs.exception.NotFoundException;
import org.example.sbs.exception.enums.ExceptionMessage;
import org.example.sbs.mapper.PlanMapper;
import org.example.sbs.model.Plan;
import org.example.sbs.repository.PlanRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PlanService {
    private final PlanRepository planRepository;
    private final PlanMapper planMapper;

    public CreatePlanResponse createPlan(CreatePlanRequest createPlanRequest) {
        return planMapper.toCreatePlanResponse(planRepository.save(planMapper.toPlan(createPlanRequest)));
    }

    public CreatePlanResponse getPlanById(Long id) {
        Plan plan = planRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(ExceptionMessage.ENTITY_NOT_FOUND.generateNotFoundEntityMessage("Plan", id)));
        return planMapper.toCreatePlanResponse(plan);
    }

    public List<CreatePlanResponse> getAllPlans() {
        return planRepository.findAll().stream()
                .map(planMapper::toCreatePlanResponse)
                .toList();
    }

    public CreatePlanResponse updatePlan(Long id, CreatePlanRequest createPlanRequest) {
        Plan plan = planRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(ExceptionMessage.ENTITY_NOT_FOUND.generateNotFoundEntityMessage("Plan", id)));
        Plan newPlan = planMapper.toPlan(createPlanRequest);
        newPlan.setId(plan.getId());
        return planMapper.toCreatePlanResponse(planRepository.save(newPlan));
    }

    public void deletePlan(Long id) {
        Plan plan = planRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(ExceptionMessage.ENTITY_NOT_FOUND.generateNotFoundEntityMessage("Plan", id)));
        planRepository.delete(plan);
    }
}
