package org.example.sbs.dto.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreateSubscriptionRequest {
    @NotNull(message = "Plan id must be not null")
    @Positive(message = "Plan id must be bigger than zero")
    private Long planId;
}
