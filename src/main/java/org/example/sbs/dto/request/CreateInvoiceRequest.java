package org.example.sbs.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class CreateInvoiceRequest {

    @NotNull(message = "Subscription id must be not null")
    private Long subscriptionId;

    @NotNull(message = "Description must be not null")
    @NotBlank(message = "Description must be not blank")
    private String description;

    @NotNull(message = "Amount must be not null")
    @Positive(message = "Amount must be bigger than zero")
    private BigDecimal amount;
}
