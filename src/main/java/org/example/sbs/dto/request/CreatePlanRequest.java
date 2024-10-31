package org.example.sbs.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Getter;
import lombok.Setter;
import org.example.sbs.enums.BillingCycle;
import org.example.sbs.exception.validator.EnumValidator;

import java.math.BigDecimal;

@Getter
@Setter
public class CreatePlanRequest {
    @NotNull(message = "Name must be not null")
    @NotBlank(message = "Name must be not blank")
    private String name;

    @NotBlank(message = "Description must be not blank")
    private String description;

    @NotNull(message = "Price must be not null")
    @Positive(message = "Price must be bigger than zero")
    private BigDecimal price;

    @EnumValidator(enumClass = BillingCycle.class, message = "Incorrect billing cycle value")
    private String billingCycle;

    @NotNull(message = "Max users must be not null")
    @Positive(message = "Max users must be bigger than zero")
    private Long maxUsers;
}
