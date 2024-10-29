package org.example.sbs.service;

import lombok.RequiredArgsConstructor;
import org.example.sbs.dto.request.CreateUserRequest;
import org.example.sbs.dto.response.CreateUserResponse;
import org.example.sbs.mapper.UserMapper;
import org.example.sbs.model.Subscription;
import org.example.sbs.model.User;
import org.example.sbs.repository.SubscriptionRepository;
import org.example.sbs.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final SubscriptionRepository subscriptionRepository;

    public CreateUserResponse createUser(CreateUserRequest request) {
        User savedUser = userRepository.save(userMapper.toUser(request));
        return userMapper.toCreateUserResponse(savedUser);
    }

    public CreateUserResponse createUser(CreateUserRequest request, Long subscriptionId) {
        Subscription subscription = subscriptionRepository.findById(subscriptionId)
                .orElseThrow(() -> new RuntimeException("Subscription with id: " + subscriptionId + " not found"));
        User savedUser = userRepository.save(userMapper.toUser(request, subscription));
        return userMapper.toCreateUserResponse(savedUser);
    }

    public CreateUserResponse getUserById(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User with id: " + id + " not found"));
        return userMapper.toCreateUserResponse(user);
    }

    public List<CreateUserResponse> getAllUsers() {
        return userRepository.findAll().stream()
                .map(userMapper::toCreateUserResponse)
                .toList();
    }

    public CreateUserResponse updateUser(Long id, CreateUserRequest request) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User with id: " + id + " not found"));
        User newUser = userMapper.toUser(request);
        newUser.setId(id);
        newUser.setSubscription(user.getSubscription());
        return userMapper.toCreateUserResponse(userRepository.save(newUser));
    }

    public void deleteUser(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User with id: " + id + " not found"));
        userRepository.delete(user);
    }
}
