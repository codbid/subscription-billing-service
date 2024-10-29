package org.example.sbs.dto.response;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class CreatePaymentResponse {
    private Long id;

    private CreateInvoiceResponse invoice;

    private String paymentMethod;

    private String status;

    private LocalDateTime createdAt;
}
