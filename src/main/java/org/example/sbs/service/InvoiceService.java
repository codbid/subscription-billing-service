package org.example.sbs.service;

import lombok.RequiredArgsConstructor;
import org.example.sbs.dto.request.CreateInvoiceRequest;
import org.example.sbs.dto.response.CreateInvoiceResponse;
import org.example.sbs.enums.InvoiceStatus;
import org.example.sbs.exception.NotFoundException;
import org.example.sbs.exception.enums.ExceptionMessage;
import org.example.sbs.mapper.InvoiceMapper;
import org.example.sbs.model.Invoice;
import org.example.sbs.repository.InvoiceRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class InvoiceService {
    private final InvoiceRepository invoiceRepository;
    private final InvoiceMapper invoiceMapper;

    public CreateInvoiceResponse createInvoice(CreateInvoiceRequest request) {
        Invoice invoice = invoiceMapper.toInvoice(request);
        invoice.setCreatedDate(LocalDate.now());
        invoice.setStatus(InvoiceStatus.AWAITING_PAYMENT);
        return invoiceMapper.toCreateInvoiceResponse(invoiceRepository.save(invoice));
    }

    public CreateInvoiceResponse getInvoiceById(Long id) {
        Invoice invoice = invoiceRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(ExceptionMessage.ENTITY_NOT_FOUND.generateNotFoundEntityMessage("Invoice", id)));
        return invoiceMapper.toCreateInvoiceResponse(invoice);
    }

    public List<CreateInvoiceResponse> getAllInvoices() {
        return invoiceRepository.findAll().stream()
                .map(invoiceMapper::toCreateInvoiceResponse)
                .toList();
    }
//
//    public CreateInvoiceResponse updateInvoice(Long id, CreateInvoiceRequest request) {
//        Invoice invoice = invoiceRepository.findById(id)
//                .orElseThrow(() -> new RuntimeException("Invoice with id: " + id + " not found"));
//        Invoice newInvoice = invoiceMapper.toInvoice(request);
//        newInvoice.setId(invoice.getId());
//        return invoiceMapper.toCreateInvoiceResponse((invoiceRepository.save(newInvoice)));
//    }
//
//    public void deleteInvoice(Long id) {
//        Invoice invoice = invoiceRepository.findById(id)
//                .orElseThrow(() -> new RuntimeException("Invoice with id: " + id + " not found"));
//        invoiceRepository.delete(invoice);
//    }
}
