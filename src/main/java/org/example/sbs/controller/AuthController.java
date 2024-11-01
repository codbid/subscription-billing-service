package org.example.sbs.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.example.sbs.dto.request.LoginRequest;
import org.example.sbs.dto.response.CreateInvoiceResponse;
import org.example.sbs.dto.response.LoginResponse;
import org.example.sbs.exception.ExceptionResponseExample;
import org.example.sbs.service.AuthService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth")
public class AuthController {
    private final AuthService authService;

    @Operation(
            summary = "Authorization attempt",
            tags = {"Users", "USER"}
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Authorization successful",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = LoginResponse.class))),
            @ApiResponse(responseCode = "401", description = "Authorization failed",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ExceptionResponseExample.class))),
    })
    @PostMapping
    public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest request) {
        return ResponseEntity.ok(authService.login(request));
    }
}
