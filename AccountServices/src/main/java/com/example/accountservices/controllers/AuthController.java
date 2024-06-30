package com.example.accountservices.controllers;

import com.example.accountservices.payload.request.LoginRequest;
import com.example.accountservices.payload.request.SignupRequest;
import com.example.accountservices.payload.request.TokenRefreshRequest;
import com.example.accountservices.services.AuthServices;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/auth")
public class AuthController {
    @Autowired
    AuthServices authServices;

    @PostMapping("/login")
    public ResponseEntity<?> authenticateUser (@Valid @RequestBody LoginRequest loginRequest){
        return  authServices.login(loginRequest);
    }

    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@Valid @RequestBody SignupRequest signUpRequest) {
        return authServices.signup(signUpRequest);
    }

    @PostMapping("/refreshToken")
    public ResponseEntity<?> refreshtoken(@Valid @RequestBody TokenRefreshRequest request) {
        return authServices.refreshToken(request);
    }

    @PostMapping("/signout")
    public ResponseEntity<?> logoutUser() {
        return authServices.logoutUser();
    }
}
