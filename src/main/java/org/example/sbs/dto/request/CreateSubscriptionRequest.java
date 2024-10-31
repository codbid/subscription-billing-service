package org.example.sbs.dto.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreateSubscriptionRequest {
    @NotNull(message = "Owner id must be not null")
    @Positive(message = "Owner must be bigger than zero")
    private Long ownerId;

    @NotNull(message = "Plan id must be not null")
    @Positive(message = "Plan id must be bigger than zero")
    private Long planId;
}
