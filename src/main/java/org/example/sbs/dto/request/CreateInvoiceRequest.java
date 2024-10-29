package org.example.sbs.dto.request;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class CreateInvoiceRequest {
    private Long subscriptionId;

    private String description;

    private BigDecimal amount;
}
