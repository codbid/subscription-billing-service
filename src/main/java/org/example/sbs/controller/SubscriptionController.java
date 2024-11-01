package org.example.sbs.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.sbs.dto.request.CreateSubscriptionRequest;
import org.example.sbs.dto.response.CreatePaymentResponse;
import org.example.sbs.dto.response.CreatePlanResponse;
import org.example.sbs.dto.response.CreateSubscriptionResponse;
import org.example.sbs.exception.ExceptionResponseExample;
import org.example.sbs.service.SubscriptionService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/subscriptions")
public class SubscriptionController {
    private final SubscriptionService subscriptionService;

    @Operation(
            summary = "Subscription creation",
            tags = {"Subscriptions", "USER"}
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Subscription created successful",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = CreateSubscriptionResponse.class))),
            @ApiResponse(responseCode = "400", description = "Bad request",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ExceptionResponseExample.class)))
    })
    @PostMapping
    public ResponseEntity<CreateSubscriptionResponse> createSubscription(@Valid @RequestBody CreateSubscriptionRequest request) {
        return ResponseEntity.ok(subscriptionService.createSubscription(request));
    }

    @Operation(
            summary = "Get subscription by then id",
            tags = {"Subscriptions", "USER"},
            parameters = {
                    @Parameter(name = "id", description = "Subscription id", required = true)
            }
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Subscription received successful",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = CreateSubscriptionResponse.class))),
            @ApiResponse(responseCode = "401", description = "No access to this subscription",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ExceptionResponseExample.class))),
            @ApiResponse(responseCode = "404", description = "Subscription not found",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ExceptionResponseExample.class)))})
    @GetMapping("/{id}")
    public ResponseEntity<CreateSubscriptionResponse> getSubscriptionById(@PathVariable Long id) {
        return ResponseEntity.ok(subscriptionService.getSubscriptionById(id));
    }

    @Operation(
            summary = "Get all subscriptions",
            tags = {"Subscriptions", "USER"}
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Subscriptions received successful",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = CreateSubscriptionResponse.class)))
    })
    @GetMapping
    public ResponseEntity<List<CreateSubscriptionResponse>> getAllSubscriptions() {
        return ResponseEntity.ok(subscriptionService.getAllSubscriptions());
    }

    @Operation(
            summary = "Update subscription by id",
            tags = {"Subscriptions", "ADMIN"},
            parameters = {
                    @Parameter(name = "id", description = "Subscription id", required = true)
            }
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Subscription updated successful",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = CreateSubscriptionResponse.class))),
            @ApiResponse(responseCode = "400", description = "Bad request",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ExceptionResponseExample.class))),
            @ApiResponse(responseCode = "401", description = "Does not have access rights",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ExceptionResponseExample.class))),
            @ApiResponse(responseCode = "404", description = "Subscription not found",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ExceptionResponseExample.class))),
    })
    @PreAuthorize("hasAuthority('ADMIN')")
    @PutMapping("/{id}")
    public ResponseEntity<CreateSubscriptionResponse> updateSubscription(@PathVariable Long id, @Valid @RequestBody CreateSubscriptionRequest request) {
        return ResponseEntity.ok(subscriptionService.updateSubscription(id, request));
    }

    @Operation(
            summary = "Delete subscription by id",
            tags = {"Subscriptions", "ADMIN"},
            parameters = {
                    @Parameter(name = "id", description = "Subscription id", required = true)
            }
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Subscription deleted successful",
                    content = @Content(schema = @Schema())),
            @ApiResponse(responseCode = "401", description = "Does not have access rights",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ExceptionResponseExample.class))),
            @ApiResponse(responseCode = "404", description = "Subscription not found",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ExceptionResponseExample.class))),
    })
    @PreAuthorize("hasAuthority('ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSubscription(@PathVariable Long id) {
        subscriptionService.deleteSubscription(id);
        return ResponseEntity.noContent().build();
    }

    @Operation(
            summary = "Pause subscription by id",
            tags = {"Subscriptions", "ADMIN"},
            parameters = {
                    @Parameter(name = "id", description = "Subscription id", required = true)
            }
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Subscription paused successful",
                    content = @Content(schema = @Schema())),
            @ApiResponse(responseCode = "401", description = "Does not have access rights",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ExceptionResponseExample.class))),
            @ApiResponse(responseCode = "404", description = "Subscription not found",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ExceptionResponseExample.class))),
    })
    @PreAuthorize("hasAuthority('ADMIN')")
    @PostMapping("/pause/{id}")
    public void pause(@PathVariable Long id) {
        subscriptionService.pauseSubscription(id);
    } //test

    @Operation(
            summary = "Activate subscription by id",
            tags = {"Subscriptions", "ADMIN"},
            parameters = {
                    @Parameter(name = "id", description = "Subscription id", required = true)
            }
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Subscription activated successful",
                    content = @Content(schema = @Schema())),
            @ApiResponse(responseCode = "401", description = "Does not have access rights",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ExceptionResponseExample.class))),
            @ApiResponse(responseCode = "404", description = "Subscription not found",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ExceptionResponseExample.class))),
    })
    @PreAuthorize("hasAuthority('ADMIN')")
    @PostMapping("/activate/{id}")
    public void activate(@PathVariable Long id) {
        subscriptionService.resumeSubscription(id);
    } //test
}
