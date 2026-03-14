package com.restaurant.service;

import com.restaurant.dto.request.LoginRequest;
import com.restaurant.dto.response.AuthResponse;
import com.restaurant.entity.Admin;
import com.restaurant.repository.AdminRepository;
import com.restaurant.security.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final AuthenticationManager authenticationManager;
    private final AdminRepository adminRepository;
    private final JwtUtil jwtUtil;
    private final PasswordEncoder passwordEncoder;

    public AuthResponse login(LoginRequest request) {
        // Spring Security tự kiểm tra username/password, ném exception nếu sai
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getUsername(), request.getPassword()));

        Admin admin = adminRepository.findByUsername(request.getUsername())
                .orElseThrow();

        String token = jwtUtil.generateToken(admin);

        return AuthResponse.builder()
                .token(token)
                .username(admin.getUsername())
                .fullName(admin.getFullName())
                .build();
    }
}
