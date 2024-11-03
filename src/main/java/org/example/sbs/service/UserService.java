package org.example.sbs.service;

import lombok.RequiredArgsConstructor;
import org.example.sbs.dto.request.AddRoleRequest;
import org.example.sbs.dto.request.CreateUserRequest;
import org.example.sbs.dto.response.CreateUserResponse;
import org.example.sbs.exception.ForbiddenException;
import org.example.sbs.exception.NotFoundException;
import org.example.sbs.exception.enums.ExceptionMessage;
import org.example.sbs.mapper.UserMapper;
import org.example.sbs.model.Role;
import org.example.sbs.model.Subscription;
import org.example.sbs.model.User;
import org.example.sbs.multitenancy.TenantContext;
import org.example.sbs.repository.RoleRepository;
import org.example.sbs.repository.SubscriptionRepository;
import org.example.sbs.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
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
        if(subscriptionId != 0) {
            Subscription subscription = subscriptionRepository.findById(subscriptionId)
                    .orElseThrow(() -> new NotFoundException(ExceptionMessage.ENTITY_NOT_FOUND.generateNotFoundEntityMessage("Subscription", subscriptionId)));

            if(!Objects.equals(subscription.getId(), TenantContext.getCurrentTenantId()))
                throw new ForbiddenException(ExceptionMessage.FORBIDDEN.getMessage());

            if(subscription.getPlan().getMaxUsers() == subscription.getUsers().size())
                throw new ForbiddenException(ExceptionMessage.USERS_LIMIT_REACHED.getMessage());
            user.setSubscription(subscription);
        }
        user.setRoles(Set.of(roleRepository.findByName("USER").orElseThrow(() -> new NotFoundException("Role with name: USER not found"))));
        user.setPassword(encoder.encode(request.getPassword()));
        return userMapper.toCreateUserResponse(userRepository.save(user));
    }

    public CreateUserResponse getUserById(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(ExceptionMessage.ENTITY_NOT_FOUND.generateNotFoundEntityMessage("User", id)));

        if(Objects.isNull(user.getSubscription()) | !Objects.equals(user.getSubscription().getId(), TenantContext.getCurrentTenantId()))
            throw new ForbiddenException(ExceptionMessage.FORBIDDEN.getMessage());

        return userMapper.toCreateUserResponse(user);
    }

    public List<CreateUserResponse> getAllUsers() {
        return userRepository.findAll().stream()
                .filter(user -> user.getSubscription() != null && Objects.equals(user.getSubscription().getId(), TenantContext.getCurrentTenantId()))
                .map(userMapper::toCreateUserResponse)
                .toList();
    }

    public CreateUserResponse updateUser(Long id, CreateUserRequest request) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(ExceptionMessage.ENTITY_NOT_FOUND.generateNotFoundEntityMessage("User", id)));

        if(Objects.isNull(user.getSubscription()) | !Objects.equals(user.getSubscription().getId(), TenantContext.getCurrentTenantId()))
            throw new ForbiddenException(ExceptionMessage.FORBIDDEN.getMessage());

        User newUser = userMapper.toUser(request);
        newUser.setId(id);
        newUser.setSubscription(user.getSubscription());
        return userMapper.toCreateUserResponse(userRepository.save(newUser));
    }

    public void deleteUser(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(ExceptionMessage.ENTITY_NOT_FOUND.generateNotFoundEntityMessage("User", id)));

        if(Objects.isNull(user.getSubscription()) || !Objects.equals(user.getSubscription().getId(), TenantContext.getCurrentTenantId()))
            throw new ForbiddenException(ExceptionMessage.FORBIDDEN.getMessage());

        userRepository.delete(user);
    }

    public CreateUserResponse addRole(Long id, AddRoleRequest request) {
        if(!Objects.equals(request.getName(), "ADMIN"))
            throw new ForbiddenException(ExceptionMessage.FORBIDDEN_ROLE.getMessage());

        Role role = roleRepository.findByName(request.getName().toUpperCase())
                .orElseThrow(() -> new NotFoundException("Role with name: " + request.getName().toUpperCase() + " not found"));
        User user = userRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(ExceptionMessage.ENTITY_NOT_FOUND.generateNotFoundEntityMessage("User", id)));

        if(Objects.isNull(user.getSubscription()) || !Objects.equals(user.getSubscription().getId(), TenantContext.getCurrentTenantId()))
            throw new ForbiddenException(ExceptionMessage.FORBIDDEN.getMessage());

        user.getRoles().add(role);
        return userMapper.toCreateUserResponse(userRepository.save(user));
    }

    public CreateUserResponse removeRole(Long id, AddRoleRequest request) {
        if(!Objects.equals(request.getName(), "ADMIN"))
            throw new ForbiddenException(ExceptionMessage.FORBIDDEN_ROLE.getMessage());

        Role role = roleRepository.findByName(request.getName().toUpperCase())
                .orElseThrow(() -> new NotFoundException("Role with name: " + request.getName().toUpperCase() + " not found"));
        User user = userRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(ExceptionMessage.ENTITY_NOT_FOUND.generateNotFoundEntityMessage("User", id)));

        if(Objects.isNull(user.getSubscription()) || !Objects.equals(user.getSubscription().getId(), TenantContext.getCurrentTenantId()))
            throw new ForbiddenException(ExceptionMessage.FORBIDDEN.getMessage());

        user.getRoles().remove(role);
        return userMapper.toCreateUserResponse(userRepository.save(user));
    }
}
