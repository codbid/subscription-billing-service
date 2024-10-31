    package org.example.sbs.controller;

    import jakarta.validation.Valid;
    import lombok.RequiredArgsConstructor;
    import org.example.sbs.dto.request.CreateInvoiceRequest;
    import org.example.sbs.dto.response.CreateInvoiceResponse;
    import org.example.sbs.service.InvoiceService;
    import org.springframework.http.ResponseEntity;
    import org.springframework.web.bind.annotation.*;

    import java.util.List;

    @RestController
    @RequiredArgsConstructor
    @RequestMapping("/invoices")
    public class InvoiceController {
        private final InvoiceService invoiceService;

        @PostMapping
        public ResponseEntity<CreateInvoiceResponse> createInvoice(@Valid @RequestBody CreateInvoiceRequest request) {
            return ResponseEntity.ok(invoiceService.createInvoice(request));
        }

        @GetMapping("/{id}")
        public ResponseEntity<CreateInvoiceResponse> getInvoiceById(@PathVariable Long id) {
            return ResponseEntity.ok(invoiceService.getInvoiceById(id));
        }

        @GetMapping
        public ResponseEntity<List<CreateInvoiceResponse>> getAllInvoices() {
            return ResponseEntity.ok(invoiceService.getAllInvoices());
        }
    //
    //    @PutMapping("/{id}")
    //    public ResponseEntity<CreateInvoiceResponse> updateInvoice(@PathVariable Long id, @RequestBody CreateInvoiceRequest request) {
    //        return ResponseEntity.ok(invoiceService.updateInvoice(id, request));
    //    }
    //
    //    @DeleteMapping("/{id}")
    //    public ResponseEntity<CreateInvoiceResponse> deleteInvoice(@PathVariable Long id) {
    //        invoiceService.deleteInvoice(id);
    //        return ResponseEntity.noContent().build();
    //    }
    }
