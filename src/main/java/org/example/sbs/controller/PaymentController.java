package org.example.sbs.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.sbs.dto.request.CreatePaymentRequest;
import org.example.sbs.dto.response.CreatePaymentResponse;
import org.example.sbs.exception.ExceptionResponseExample;
import org.example.sbs.service.PaymentService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/payments")
public class PaymentController {
    private final PaymentService paymentService;

    @Operation(
            summary = "Payment creation",
            tags = {"Payments", "USER"}
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Payment created successful",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = CreatePaymentResponse.class))),
            @ApiResponse(responseCode = "400", description = "Bad request",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ExceptionResponseExample.class)))
    })
    @PostMapping
    public ResponseEntity<CreatePaymentResponse> createPayment(@Valid @RequestBody CreatePaymentRequest request) {
        return ResponseEntity.ok(paymentService.createPayment(request));
    }

    @Operation(
            summary = "Get payment by then id",
            tags = {"Payments", "USER"},
            parameters = {
                    @Parameter(name = "id", description = "Payment id", required = true)
            }
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Payment received successful",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = CreatePaymentResponse.class))),
            @ApiResponse(responseCode = "401", description = "No access to this payment",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ExceptionResponseExample.class))),
            @ApiResponse(responseCode = "404", description = "Payment not found",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ExceptionResponseExample.class)))
    })
    @GetMapping("/{id}")
    public ResponseEntity<CreatePaymentResponse> getPaymentById(@PathVariable Long id) {
        return ResponseEntity.ok(paymentService.getPaymentById(id));
    }

    @Operation(
            summary = "Get all payments",
            tags = {"Payments", "USER"}
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Payments received successful",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = CreatePaymentResponse.class))),
            @ApiResponse(responseCode = "401", description = "Does not have access rights",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ExceptionResponseExample.class))),
    })
    @GetMapping
    public ResponseEntity<List<CreatePaymentResponse>> getAllPayments() {
        return ResponseEntity.ok(paymentService.getAllPayments());
    }
}
