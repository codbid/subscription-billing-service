package org.example.sbs.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AddRoleRequest {

    @NotNull(message = "Name must be not null")
    @NotBlank(message = "Name must be not blank")
    private String name;
}
