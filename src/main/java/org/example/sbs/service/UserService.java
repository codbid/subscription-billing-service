package org.example.sbs.service;

import lombok.RequiredArgsConstructor;
import org.example.sbs.dto.request.AddRoleReuqest;
import org.example.sbs.dto.request.CreateUserRequest;
import org.example.sbs.dto.response.CreateUserResponse;
import org.example.sbs.exception.NotFoundException;
import org.example.sbs.exception.enums.ExceptionMessage;
import org.example.sbs.mapper.UserMapper;
import org.example.sbs.model.Role;
import org.example.sbs.model.Subscription;
import org.example.sbs.model.User;
import org.example.sbs.repository.RoleRepository;
import org.example.sbs.repository.SubscriptionRepository;
import org.example.sbs.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final SubscriptionRepository subscriptionRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder encoder;

    public CreateUserResponse createUser(CreateUserRequest request, Long subscriptionId) {
        User user = userMapper.toUser(request);
        if(subscriptionId != null) {
            Subscription subscription = subscriptionRepository.findById(subscriptionId)
                    .orElseThrow(() -> new NotFoundException(ExceptionMessage.ENTITY_NOT_FOUND.generateNotFoundEntityMessage("Subscription", subscriptionId)));
            user.setSubscription(subscription);
        }
        user.setRoles(Set.of(roleRepository.findByName("USER").orElseThrow(() -> new NotFoundException("Role with name: USER not found"))));
        user.setPassword(encoder.encode(request.getPassword()));
        return userMapper.toCreateUserResponse(userRepository.save(user));
    }

    public CreateUserResponse getUserById(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(ExceptionMessage.ENTITY_NOT_FOUND.generateNotFoundEntityMessage("User", id)));
        return userMapper.toCreateUserResponse(user);
    }

    public List<CreateUserResponse> getAllUsers() {
        return userRepository.findAll().stream()
                .map(userMapper::toCreateUserResponse)
                .toList();
    }

    public CreateUserResponse updateUser(Long id, CreateUserRequest request) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(ExceptionMessage.ENTITY_NOT_FOUND.generateNotFoundEntityMessage("User", id)));
        User newUser = userMapper.toUser(request);
        newUser.setId(id);
        newUser.setSubscription(user.getSubscription());
        return userMapper.toCreateUserResponse(userRepository.save(newUser));
    }

    public void deleteUser(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(ExceptionMessage.ENTITY_NOT_FOUND.generateNotFoundEntityMessage("User", id)));
        userRepository.delete(user);
    }

    public CreateUserResponse addRole(Long id, AddRoleReuqest request) {
        Role role = roleRepository.findByName(request.getName().toUpperCase())
                .orElseThrow(() -> new NotFoundException("Role with name: " + request.getName().toUpperCase() + " not found"));
        User user = userRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(ExceptionMessage.ENTITY_NOT_FOUND.generateNotFoundEntityMessage("User", id)));
        user.getRoles().add(role);
        return userMapper.toCreateUserResponse(userRepository.save(user));
    }

    public CreateUserResponse removeRole(Long id, AddRoleReuqest request) {
        Role role = roleRepository.findByName(request.getName().toUpperCase())
                .orElseThrow(() -> new NotFoundException("Role with name: " + request.getName().toUpperCase() + " not found"));
        User user = userRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(ExceptionMessage.ENTITY_NOT_FOUND.generateNotFoundEntityMessage("User", id)));
        user.getRoles().remove(role);
        return userMapper.toCreateUserResponse(userRepository.save(user));
    }
}
