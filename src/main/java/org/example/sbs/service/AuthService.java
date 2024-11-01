package org.example.sbs.service;

import lombok.RequiredArgsConstructor;
import org.example.sbs.dto.request.LoginRequest;
import org.example.sbs.dto.response.LoginResponse;
import org.example.sbs.security.UserDetailsImpl;
import org.example.sbs.security.jwt.JwtUtil;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;

    public LoginResponse login(LoginRequest request) {
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword());
        Authentication authentication = authenticationManager.authenticate(authenticationToken);

        SecurityContextHolder.getContext().setAuthentication(authentication);

        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();

        LoginResponse response = new LoginResponse();
        response.setToken(jwtUtil.generateToken(userDetails.getUsername(), userDetails.getId(), userDetails.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority).collect(Collectors.toSet())));

        return response;
    }
}
