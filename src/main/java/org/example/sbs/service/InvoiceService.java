package org.example.sbs.service;

import lombok.RequiredArgsConstructor;
import org.example.sbs.dto.request.CreateInvoiceRequest;
import org.example.sbs.dto.response.CreateInvoiceResponse;
import org.example.sbs.enums.InvoiceStatus;
import org.example.sbs.exception.ForbiddenException;
import org.example.sbs.exception.NotFoundException;
import org.example.sbs.exception.enums.ExceptionMessage;
import org.example.sbs.mapper.InvoiceMapper;
import org.example.sbs.model.Invoice;
import org.example.sbs.multitenancy.TenantContext;
import org.example.sbs.repository.InvoiceRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class InvoiceService {
    private final InvoiceRepository invoiceRepository;
    private final InvoiceMapper invoiceMapper;

    public CreateInvoiceResponse createInvoice(CreateInvoiceRequest request) {
        if (!Objects.equals(request.getSubscriptionId(), TenantContext.getCurrentTenantId()))
            throw new ForbiddenException(ExceptionMessage.FORBIDDEN.getMessage());

        Invoice invoice = invoiceMapper.toInvoice(request);
        invoice.setCreatedDate(LocalDate.now());
        invoice.setStatus(InvoiceStatus.AWAITING_PAYMENT);
        return invoiceMapper.toCreateInvoiceResponse(invoiceRepository.save(invoice));
    }

    public CreateInvoiceResponse getInvoiceById(Long id) {
        Invoice invoice = invoiceRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(ExceptionMessage.ENTITY_NOT_FOUND.generateNotFoundEntityMessage("Invoice", id)));

        if (!Objects.equals(invoice.getSubscription().getId(), TenantContext.getCurrentTenantId()))
            throw new ForbiddenException(ExceptionMessage.FORBIDDEN.getMessage());

        return invoiceMapper.toCreateInvoiceResponse(invoice);
    }

    public List<CreateInvoiceResponse> getAllInvoices() {
        return invoiceRepository.findAll().stream()
                .map(invoiceMapper::toCreateInvoiceResponse)
                .filter(invoice -> invoice.getSubscription().getId().equals(TenantContext.getCurrentTenantId()))
                .toList();
    }
}
