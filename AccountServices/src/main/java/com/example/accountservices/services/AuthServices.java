package com.example.accountservices.services;

import com.example.accountservices.payload.request.LoginRequest;
import com.example.accountservices.payload.request.SignupRequest;
import com.example.accountservices.payload.request.TokenRefreshRequest;
import org.springframework.http.ResponseEntity;

public interface AuthServices {
    ResponseEntity<?> login(LoginRequest loginRequest);
    ResponseEntity<?> signup(SignupRequest signupRequest);
    ResponseEntity<?> refreshToken(TokenRefreshRequest tokenRefreshRequest);
    ResponseEntity<?> logoutUser();
}
