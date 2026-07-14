package com.example.messenger.auth.service;

import com.example.messenger.auth.dto.AuthResponse;
import com.example.messenger.auth.dto.LoginRequest;
import com.example.messenger.auth.dto.RegisterRequest;

public interface AuthService{
    void register(RegisterRequest request);
    AuthResponse login(LoginRequest request);
}