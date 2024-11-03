package org.example.sbs.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.sbs.dto.request.CreatePlanRequest;
import org.example.sbs.dto.response.CreatePlanResponse;
import org.example.sbs.exception.ExceptionResponseExample;
import org.example.sbs.service.PlanService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/plans")
public class PlanController {
    private final PlanService planService;

    @Operation(
            summary = "Plan creation",
            tags = {"Plans", "SUPER_ADMIN"}
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Plan created successful",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = CreatePlanResponse.class))),
            @ApiResponse(responseCode = "400", description = "Bad request",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ExceptionResponseExample.class)))
    })
    @PreAuthorize("hasAuthority('SUPER_ADMIN')")
    @PostMapping
    public ResponseEntity<CreatePlanResponse> createPlan(@Valid @RequestBody CreatePlanRequest request) {
        return ResponseEntity.ok(planService.createPlan(request));
    }

    @Operation(
            summary = "Get plan by then id",
            tags = {"Plans", "USER"},
            parameters = {
                    @Parameter(name = "id", description = "Plan id", required = true)
            }
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Plan received successful",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = CreatePlanResponse.class))),
            @ApiResponse(responseCode = "401", description = "No access to this plan",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ExceptionResponseExample.class))),
            @ApiResponse(responseCode = "404", description = "Plan not found",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ExceptionResponseExample.class)))})
    @GetMapping("/{id}")
    public ResponseEntity<CreatePlanResponse> getPlanById(@PathVariable Long id) {
        return ResponseEntity.ok(planService.getPlanById(id));
    }

    @Operation(
            summary = "Get all plans",
            tags = {"Plans", "USER"}
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Plans received successful",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = CreatePlanResponse.class)))
    })
    @GetMapping
    public ResponseEntity<List<CreatePlanResponse>> getAllPlans() {
        return ResponseEntity.ok(planService.getAllPlans());
    }

    @Operation(
            summary = "Update plan by id",
            tags = {"Plans", "SUPER_ADMIN"},
            parameters = {
                    @Parameter(name = "id", description = "Plan id", required = true)
            }
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Plan updated successful",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = CreatePlanResponse.class))),
            @ApiResponse(responseCode = "400", description = "Bad request",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ExceptionResponseExample.class))),
            @ApiResponse(responseCode = "401", description = "Does not have access rights",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ExceptionResponseExample.class))),
            @ApiResponse(responseCode = "404", description = "Plan not found",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ExceptionResponseExample.class))),
    })
    @PreAuthorize("hasAuthority('SUPER_ADMIN')")
    @PutMapping("/{id}")
    public ResponseEntity<CreatePlanResponse> updatePlan(@PathVariable Long id, @Valid @RequestBody CreatePlanRequest request) {
        return ResponseEntity.ok(planService.updatePlan(id, request));
    }

    @Operation(
            summary = "Delete plan by id",
            tags = {"Plans", "SUPER_ADMIN"},
            parameters = {
                    @Parameter(name = "id", description = "Plan id", required = true)
            }
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Plan deleted successful",
                    content = @Content(schema = @Schema())),
            @ApiResponse(responseCode = "401", description = "Does not have access rights",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ExceptionResponseExample.class))),
            @ApiResponse(responseCode = "404", description = "Plan not found",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ExceptionResponseExample.class))),
    })
    @PreAuthorize("hasAuthority('SUPER_ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePlan(@PathVariable Long id) {
        planService.deletePlan(id);
        return ResponseEntity.noContent().build();
    }
}
