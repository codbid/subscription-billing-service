package org.example.sbs.dto.response;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class CreateSubscriptionResponse {
    private Long id;

    private CreateUserResponse owner;

    private CreatePlanResponse plan;

    private String status;

    private LocalDate startDate;

    private LocalDate endDate;
}
