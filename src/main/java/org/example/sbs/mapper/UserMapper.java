package org.example.sbs.mapper;

import org.example.sbs.dto.request.CreateUserRequest;
import org.example.sbs.dto.response.CreateUserResponse;
import org.example.sbs.model.Role;
import org.example.sbs.model.User;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.stream.Collectors;

@Component
public class UserMapper {

    public User toUser(CreateUserRequest request) {
        User user = new User();
        user.setLogin(request.getLogin());
        user.setName(request.getName());
        user.setEmail(request.getEmail());
        user.setPassword(request.getPassword());
        return user;
    }

    public CreateUserResponse toCreateUserResponse(User user) {
        CreateUserResponse response = new CreateUserResponse();
        Optional.ofNullable(user.getSubscription()).ifPresent(subscription -> response.setSubscriptionId(subscription.getId()));
        response.setId(user.getId());
        response.setLogin(user.getLogin());
        response.setName(user.getName());
        response.setEmail(user.getEmail());
        response.setRoles(user.getRoles().stream()
                .map(Role::getName)
                .toList());
        return response;
    }
}
