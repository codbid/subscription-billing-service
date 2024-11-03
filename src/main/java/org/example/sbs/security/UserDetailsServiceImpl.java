package org.example.sbs.security;

import lombok.RequiredArgsConstructor;
import org.example.sbs.exception.NotFoundException;
import org.example.sbs.model.User;
import org.example.sbs.repository.UserRepository;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByLogin(username)
                .orElseThrow(() -> new NotFoundException("User not found"));
        List<SimpleGrantedAuthority> roles = user.getRoles().stream()
                .map(role -> new SimpleGrantedAuthority(role.getName()))
                .toList();
        Long tenantId = user.getSubscription() != null ? user.getSubscription().getId() : null;

        return UserDetailsImpl.builder().id(user.getId())
                .username(user.getLogin())
                .authorities(roles)
                .password(user.getPassword())
                .tenantId(tenantId).build();
    }
}
