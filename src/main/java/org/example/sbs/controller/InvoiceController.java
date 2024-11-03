    package org.example.sbs.controller;

    import io.swagger.v3.oas.annotations.Operation;
    import io.swagger.v3.oas.annotations.Parameter;
    import io.swagger.v3.oas.annotations.media.Content;
    import io.swagger.v3.oas.annotations.media.Schema;
    import io.swagger.v3.oas.annotations.responses.ApiResponse;
    import io.swagger.v3.oas.annotations.responses.ApiResponses;
    import io.swagger.v3.oas.annotations.tags.Tag;
    import jakarta.validation.Valid;
    import lombok.RequiredArgsConstructor;
    import org.example.sbs.dto.request.CreateInvoiceRequest;
    import org.example.sbs.dto.response.CreateInvoiceResponse;
    import org.example.sbs.exception.ExceptionResponseExample;
    import org.example.sbs.service.InvoiceService;
    import org.springframework.http.ResponseEntity;
    import org.springframework.security.access.prepost.PreAuthorize;
    import org.springframework.web.bind.annotation.*;

    import java.util.List;

    @RestController
    @RequiredArgsConstructor
    @RequestMapping("/api/invoices")
    public class InvoiceController {
        private final InvoiceService invoiceService;

        @Operation(
                summary = "Invoice creation",
                tags = {"Invoices", "ADMIN"}
        )
        @ApiResponses(value = {
                @ApiResponse(responseCode = "200", description = "Invoice created successful",
                        content = @Content(mediaType = "application/json", schema = @Schema(implementation = CreateInvoiceResponse.class))),
                @ApiResponse(responseCode = "400", description = "Bad request",
                        content = @Content(mediaType = "application/json", schema = @Schema(implementation = ExceptionResponseExample.class)))
        })
        @PreAuthorize("hasAuthority('ADMIN')")
        @PostMapping
        public ResponseEntity<CreateInvoiceResponse> createInvoice(@Valid @RequestBody CreateInvoiceRequest request) {
            return ResponseEntity.ok(invoiceService.createInvoice(request));
        }

        @Operation(
                summary = "Get invoice by then id",
                tags = {"Invoices", "ADMIN"},
                parameters = {
                        @Parameter(name = "id", description = "Invoice id", required = true)
                }
        )
        @ApiResponses(value = {
                @ApiResponse(responseCode = "200", description = "Invoice received successful",
                        content = @Content(mediaType = "application/json", schema = @Schema(implementation = CreateInvoiceResponse.class))),
                @ApiResponse(responseCode = "401", description = "No access to this invoice",
                        content = @Content(mediaType = "application/json", schema = @Schema(implementation = ExceptionResponseExample.class))),
                @ApiResponse(responseCode = "404", description = "Invoice not found",
                        content = @Content(mediaType = "application/json", schema = @Schema(implementation = ExceptionResponseExample.class)))
        })
        @PreAuthorize("hasAuthority('ADMIN')")
        @GetMapping("/{id}")
        public ResponseEntity<CreateInvoiceResponse> getInvoiceById(@PathVariable Long id) {
            return ResponseEntity.ok(invoiceService.getInvoiceById(id));
        }

        @Operation(
                summary = "Get all invoices",
                tags = {"Invoices", "ADMIN"}
        )
        @ApiResponses(value = {
                @ApiResponse(responseCode = "200", description = "Invoices received successful",
                        content = @Content(mediaType = "application/json", schema = @Schema(implementation = CreateInvoiceResponse.class))),
                @ApiResponse(responseCode = "401", description = "Does not have access rights",
                        content = @Content(mediaType = "application/json", schema = @Schema(implementation = ExceptionResponseExample.class))),
        })
        @PreAuthorize("hasAuthority('ADMIN')")
        @GetMapping
        public ResponseEntity<List<CreateInvoiceResponse>> getAllInvoices() {
            return ResponseEntity.ok(invoiceService.getAllInvoices());
        }
    }
