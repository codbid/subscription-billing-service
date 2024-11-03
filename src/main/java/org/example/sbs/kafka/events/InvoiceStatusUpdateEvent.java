package org.example.sbs.kafka.events;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.example.sbs.enums.InvoiceStatus;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class InvoiceStatusUpdateEvent {
    private Long invoiceId;

    private InvoiceStatus status;
}
