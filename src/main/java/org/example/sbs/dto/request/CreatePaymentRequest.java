package org.example.sbs.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import org.example.sbs.enums.PaymentMethod;
import org.example.sbs.exception.validator.EnumValidator;

@Getter
@Setter
public class CreatePaymentRequest {
    @NotNull(message = "Invoice id must be not null")
    private Long invoiceId;

    @EnumValidator(enumClass = PaymentMethod.class, message = "Incorrect payment method")
    private String paymentMethod;
}
