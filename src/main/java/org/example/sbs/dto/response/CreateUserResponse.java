package org.example.sbs.dto.response;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class CreateUserResponse {
    private Long id;

    private Long subscriptionId;

    private String login;

    private String name;

    private String email;

    private List<String> roles;
}