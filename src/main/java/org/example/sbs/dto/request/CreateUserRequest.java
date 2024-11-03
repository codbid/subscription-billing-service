package org.example.sbs.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreateUserRequest {
    @NotNull(message = "Login must be not null")
    @NotBlank(message = "Login must be not blank")
    private String login;

    @NotNull(message = "Name must be not null")
    @NotBlank(message = "Name must be not blank")
    private String name;

    @NotNull(message = "Email must be not null")
    @Email(message = "Incorrect email format")
    private String email;

    @NotNull(message = "Password must be not null")
    @NotBlank(message = "Password must be not blank")
    private String password;
}
