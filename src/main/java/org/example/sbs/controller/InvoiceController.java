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
    import org.springframework.web.bind.annotation.*;

    import java.util.List;

    @RestController
    @RequiredArgsConstructor
    @RequestMapping("/api/invoices")
    @Tag(name = "Invoice controller", description = "Invoices management")
    public class InvoiceController {
        private final InvoiceService invoiceService;

        @PostMapping
        @Operation(
                summary = "Invoice creation",
                tags = {"Invoices", "USER"}
        )
        @ApiResponses(value = {
                @ApiResponse(responseCode = "200", description = "Invoice created successful",
                        content = @Content(mediaType = "application/json", schema = @Schema(implementation = CreateInvoiceResponse.class))),
                @ApiResponse(responseCode = "400", description = "Bad request",
                        content = @Content(mediaType = "application/json", schema = @Schema(implementation = ExceptionResponseExample.class)))
        })
        public ResponseEntity<CreateInvoiceResponse> createInvoice(@Valid @RequestBody CreateInvoiceRequest request) {
            return ResponseEntity.ok(invoiceService.createInvoice(request));
        }

        @Operation(
                summary = "Get invoice by then id",
                tags = {"Invoices", "USER"},
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
        @GetMapping("/{id}")
        public ResponseEntity<CreateInvoiceResponse> getInvoiceById(@PathVariable Long id) {
            return ResponseEntity.ok(invoiceService.getInvoiceById(id));
        }

        @Operation(
                summary = "Get all invoices",
                tags = {"Invoices", "USER"}
        )
        @ApiResponses(value = {
                @ApiResponse(responseCode = "200", description = "Invoices received successful",
                        content = @Content(mediaType = "application/json", schema = @Schema(implementation = CreateInvoiceResponse.class))),
                @ApiResponse(responseCode = "401", description = "Does not have access rights",
                        content = @Content(mediaType = "application/json", schema = @Schema(implementation = ExceptionResponseExample.class))),
        })
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
