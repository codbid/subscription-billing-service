package org.example.sbs.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LoginRequest {
    @NotNull(message = "Username must be not null")
    @NotBlank(message = "Username must be not blank")
    private String username;

    @NotNull(message = "Password must be not null")
    @NotBlank(message = "Password must be not blank")
    private String password;
}
