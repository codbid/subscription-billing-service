package org.example.sbs.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.sbs.dto.request.AddRoleReuqest;
import org.example.sbs.dto.request.CreateUserRequest;
import org.example.sbs.dto.response.CreatePaymentResponse;
import org.example.sbs.dto.response.CreatePlanResponse;
import org.example.sbs.dto.response.CreateUserResponse;
import org.example.sbs.exception.ExceptionResponseExample;
import org.example.sbs.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/users")
public class UserController {
    private final UserService userService;

    @Operation(
            summary = "User creation with addition to subscription (optional)",
            tags = {"Users", "Subscriptions"},
            parameters = {
                    @Parameter(name = "subscription_id", description = "Subscription id")
            }
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "User created successful",
                    content = @Content(schema = @Schema(implementation = CreateUserResponse.class))),
            @ApiResponse(responseCode = "400", description = "Bad request",
                    content = @Content(schema = @Schema(implementation = ExceptionResponseExample.class))),
            @ApiResponse(responseCode = "404", description = "Subscription not found",
                    content = @Content(schema = @Schema(implementation = ExceptionResponseExample.class)))
    })
    @PostMapping
    public ResponseEntity<CreateUserResponse> createUser(@RequestParam(defaultValue = "null", name = "subscription_id") Long subscriptionId, @RequestBody CreateUserRequest request) {
        return ResponseEntity.ok(userService.createUser(request, subscriptionId));
    }

    @Operation(
            summary = "Get user by then id",
            tags = {"Users"},
            parameters = {
                    @Parameter(name = "id", description = "User id", required = true)
            }
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User received successful",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = CreateUserResponse.class))),
            @ApiResponse(responseCode = "401", description = "No access to this user",
                    content = @Content(schema = @Schema(implementation = ExceptionResponseExample.class))),
            @ApiResponse(responseCode = "404", description = "User not found",
                    content = @Content(schema = @Schema(implementation = ExceptionResponseExample.class)))})
    @GetMapping("/{id}")
    public ResponseEntity<CreateUserResponse> getUserById(@PathVariable Long id) {
        return ResponseEntity.ok(userService.getUserById(id));
    }

    @Operation(
            summary = "Get all users",
            tags = {"Users"}
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Users received successful",
                    content = @Content(schema = @Schema(implementation = CreateUserResponse.class)))
    })
    @GetMapping
    public ResponseEntity<List<CreateUserResponse>> getAllUsers() {
        return ResponseEntity.ok(userService.getAllUsers());
    }

    @Operation(
            summary = "Update user by id",
            tags = {"Users"},
            parameters = {
                    @Parameter(name = "id", description = "User id", required = true)
            }
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "User updated successful",
                    content = @Content(schema = @Schema(implementation = CreateUserResponse.class))),
            @ApiResponse(responseCode = "400", description = "Bad request",
                    content = @Content(schema = @Schema(implementation = ExceptionResponseExample.class))),
            @ApiResponse(responseCode = "401", description = "Does not have access rights",
                    content = @Content(schema = @Schema(implementation = ExceptionResponseExample.class))),
            @ApiResponse(responseCode = "404", description = "User not found",
                    content = @Content(schema = @Schema(implementation = ExceptionResponseExample.class))),
    })
    @PutMapping("/{id}")
    public ResponseEntity<CreateUserResponse> updateUser(@PathVariable Long id, @Valid @RequestBody CreateUserRequest request) {
        return ResponseEntity.ok(userService.updateUser(id, request));
    }

    @Operation(
            summary = "Delete user by id",
            tags = {"Users", "ADMIN"},
            parameters = {
                    @Parameter(name = "id", description = "User id", required = true)
            }
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "User deleted successful",
                    content = @Content(schema = @Schema())),
            @ApiResponse(responseCode = "401", description = "Does not have access rights",
                    content = @Content(schema = @Schema(implementation = ExceptionResponseExample.class))),
            @ApiResponse(responseCode = "404", description = "User not found",
                    content = @Content(schema = @Schema(implementation = ExceptionResponseExample.class))),
    })
    @PreAuthorize("hasAuthority('ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }

    @Operation(
            summary = "Add role to user by id",
            tags = {"Users", "Roles", "ADMIN"},
            parameters = {
                    @Parameter(name = "id", description = "User id", required = true)
            }
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Role added successful",
                    content = @Content(schema = @Schema())),
            @ApiResponse(responseCode = "401", description = "Does not have access rights",
                    content = @Content(schema = @Schema(implementation = ExceptionResponseExample.class))),
            @ApiResponse(responseCode = "404", description = "User or role not found",
                    content = @Content(schema = @Schema(implementation = ExceptionResponseExample.class))),
    })
    @PreAuthorize("hasAuthority('ADMIN')")
    @PutMapping("/{id}/role")
    public ResponseEntity<CreateUserResponse> addRole(@PathVariable Long id, @Valid @RequestBody AddRoleReuqest request) {
        return ResponseEntity.ok(userService.addRole(id, request));
    }

    @Operation(
            summary = "Remove user role by id",
            tags = {"Users", "Roles", "ADMIN"},
            parameters = {
                    @Parameter(name = "id", description = "User id", required = true)
            }
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Role removed successful",
                    content = @Content(schema = @Schema())),
            @ApiResponse(responseCode = "401", description = "Does not have access rights",
                    content = @Content(schema = @Schema(implementation = ExceptionResponseExample.class))),
            @ApiResponse(responseCode = "404", description = "User or role not found",
                    content = @Content(schema = @Schema(implementation = ExceptionResponseExample.class))),
    })
    @PreAuthorize("hasAuthority('ADMIN')")
    @DeleteMapping("/{id}/role")
    public ResponseEntity<CreateUserResponse> removeRole(@PathVariable Long id, @Valid @RequestBody AddRoleReuqest request) {
        return ResponseEntity.ok(userService.removeRole(id, request));
    }
}
