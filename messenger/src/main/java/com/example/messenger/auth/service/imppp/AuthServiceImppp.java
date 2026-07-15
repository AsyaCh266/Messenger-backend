package com.example.messenger.auth.service.imppp;

import com.example.messenger.auth.dto.AuthResponse;
import com.example.messenger.auth.dto.LoginRequest;
import com.example.messenger.auth.dto.RegisterRequest;
import com.example.messenger.auth.service.AuthService;
import com.example.messenger.security.JwtService;
import com.example.messenger.user.entity.User;
import com.example.messenger.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class AuthServiceImppp implements AuthService{
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    @Override
    public void register(RegisterRequest request){
        if (userRepository.existsByUsername(request.getUsername())){
            throw new RuntimeException("Username already exists");
        }

        User user = User.builder()
                .username(request.getUsername())
                .email(request.getEmail())
                .phone(request.getPhone())
                .passwordHash(passwordEncoder.encode(request.getPassword()))
                .createdAt(LocalDateTime.now())
                .build();
        userRepository.save(user);
    }

    @Override
    public AuthResponse login(LoginRequest request){
        User user = userRepository
                .findByUsername(request.getUsername())
                .orElseThrow(() -> new RuntimeException("User not found"));
        boolean passwordMatches = passwordEncoder.matches(request.getPassword(), user.getPasswordHash());

        if (!passwordMatches){
            throw new RuntimeException("Invalid password");
        }

        String token = jwtService.generateToken(user.getUsername());
        return new AuthResponse(token);
    }
}