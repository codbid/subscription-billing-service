package org.example.sbs.dto.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreateUserRequest {
    private String login;
    private String name;
    private String email;
    private String password;
}
