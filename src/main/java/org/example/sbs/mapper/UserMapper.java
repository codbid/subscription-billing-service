package org.example.sbs.mapper;

import org.example.sbs.dto.request.CreateUserRequest;
import org.example.sbs.dto.response.CreateUserResponse;
import org.example.sbs.model.Subscription;
import org.example.sbs.model.User;
import org.springframework.stereotype.Component;

import java.util.Optional;

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

    public User toUser(CreateUserRequest request, Subscription subscription) {
        User user = new User();
        user.setSubscription(subscription);
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
        return response;
    }
}
