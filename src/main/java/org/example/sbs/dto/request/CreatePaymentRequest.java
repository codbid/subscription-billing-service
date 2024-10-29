package org.example.sbs.dto.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreatePaymentRequest {
    private Long invoiceId;

    private String paymentMethod;
}
