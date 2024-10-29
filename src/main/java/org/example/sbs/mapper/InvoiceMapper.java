package org.example.sbs.mapper;

import lombok.RequiredArgsConstructor;
import org.example.sbs.dto.request.CreateInvoiceRequest;
import org.example.sbs.dto.response.CreateInvoiceResponse;
import org.example.sbs.model.Invoice;
import org.example.sbs.model.Subscription;
import org.example.sbs.repository.SubscriptionRepository;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class InvoiceMapper {
    private final SubscriptionRepository subscriptionRepository;
    private final SubscriptionMapper subscriptionMapper;


    public Invoice toInvoice(CreateInvoiceRequest request) {
        Invoice invoice = new Invoice();
        Subscription subscription = subscriptionRepository.findById(request.getSubscriptionId())
                .orElseThrow(() -> new RuntimeException("Subscription with id: " + request.getSubscriptionId() + " not found"));
        invoice.setSubscription(subscription);
        invoice.setDescription(request.getDescription());
        invoice.setAmount(request.getAmount());
        return invoice;
    }

    public CreateInvoiceResponse toCreateInvoiceResponse(Invoice invoice) {
        CreateInvoiceResponse response = new CreateInvoiceResponse();
        response.setId(invoice.getId());
        response.setSubscription(subscriptionMapper.toCreateSubscriptionResponse(invoice.getSubscription()));
        response.setDescription(invoice.getDescription());
        response.setAmount(invoice.getAmount());
        response.setStatus(invoice.getStatus().name());
        response.setCreatedDate(invoice.getCreatedDate());
        return response;
    }
}
