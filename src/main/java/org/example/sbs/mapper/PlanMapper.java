package org.example.sbs.mapper;

import org.example.sbs.dto.request.CreatePlanRequest;
import org.example.sbs.dto.response.CreatePlanResponse;
import org.example.sbs.enums.BillingCycle;
import org.example.sbs.model.Plan;
import org.springframework.stereotype.Component;

@Component
public class PlanMapper {
    public Plan toPlan(CreatePlanRequest request) {
        Plan plan = new Plan();
        plan.setName(request.getName());
        plan.setDescription(request.getDescription());
        plan.setPrice(request.getPrice());
        plan.setBillingCycle(BillingCycle.valueOf(request.getBillingCycle()));
        plan.setMaxUsers(request.getMaxUsers());
        return plan;
    }

    public CreatePlanResponse toCreatePlanResponse(Plan plan) {
        CreatePlanResponse response = new CreatePlanResponse();
        response.setId(plan.getId());
        response.setName(plan.getName());
        response.setDescription(plan.getDescription());
        response.setPrice(plan.getPrice());
        response.setBillingCycle(plan.getBillingCycle().name());
        response.setMaxUsers(plan.getMaxUsers());
        return response;
    }
}
