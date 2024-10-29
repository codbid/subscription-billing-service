package org.example.sbs.dto.request;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class CreatePlanRequest {
    private String name;

    private String description;

    private BigDecimal price;

    private String billingCycle;

    private Long maxUsers;
}
