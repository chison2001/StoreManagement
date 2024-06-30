package com.example.accountservices.services.impl;

import com.example.accountservices.entities.Account;
import com.example.accountservices.entities.Role;
import com.example.accountservices.entities.User;
import com.example.accountservices.exception.TokenRefreshException;
import com.example.accountservices.payload.request.LoginRequest;
import com.example.accountservices.payload.request.SignupRequest;
import com.example.accountservices.payload.request.TokenRefreshRequest;
import com.example.accountservices.payload.response.JwtResponse;
import com.example.accountservices.payload.response.MessageResponse;
import com.example.accountservices.payload.response.TokenRefreshResponse;
import com.example.accountservices.repositories.RoleRepository;
import com.example.accountservices.repositories.UserRepository;
import com.example.accountservices.security.jwt.JwtUtils;
import com.example.accountservices.security.services.RefreshTokenService;
import com.example.accountservices.security.services.UserDetailsImpl;
import com.example.accountservices.services.AuthServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class AuthServicesImpl implements AuthServices {
    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    UserRepository userRepository;

    @Autowired
    RoleRepository roleRepository;

    @Autowired
    PasswordEncoder encoder;

    @Autowired
    JwtUtils jwtUtils;

    @Autowired
    RefreshTokenService refreshTokenService;
    @Override
    public ResponseEntity<?> login(LoginRequest loginRequest) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(loginRequest.getEmail(), loginRequest.getPassword()));

            SecurityContextHolder.getContext().setAuthentication(authentication);

            UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();

            String jwt = jwtUtils.generateJwtToken(userDetails);

            String role = userDetails.getAuthorities().iterator().next().getAuthority();

            Account refreshToken = refreshTokenService.createRefreshToken(userDetails.getId().toString());

            return ResponseEntity.ok(new JwtResponse(jwt, refreshToken.getRefreshToken(), userDetails.getId().toString(),
                    userDetails.getUsername(), role));
        } catch (Exception e) {
            System.out.println("Authentication failed: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Authentication failed");
        }
    }

    @Override
    public ResponseEntity<?> signup(SignupRequest signUpRequest) {
        if (userRepository.existsByEmail(signUpRequest.getEmail())) {
            return ResponseEntity.badRequest().body(new MessageResponse("Error: Email is already in use!"));
        }

        User user = new User();
        user.setName(signUpRequest.getName());
        user.setEmail(signUpRequest.getEmail());
        user.setHashedPassword(encoder.encode(signUpRequest.getPassword()));
        user.setCreatedAt(LocalDateTime.now());
        user.setUpdatedAt(LocalDateTime.now());

        Role userRole = roleRepository.findByName("user");
        user.setRole(userRole);

        userRepository.save(user);
        return ResponseEntity.ok(new MessageResponse("User registered successfully!"));
    }

    @Override
    public ResponseEntity<?> refreshToken(TokenRefreshRequest tokenRefreshRequest) {
        String requestRefreshToken = tokenRefreshRequest.getRefreshToken();

        return refreshTokenService.findByToken(requestRefreshToken)
                .map(refreshTokenService::verifyExpiration)
                .map(Account::getUser)
                .map(user -> {
                    String token = jwtUtils.generateTokenFromUsername(user.getEmail());
                    return ResponseEntity.ok(new TokenRefreshResponse(token, requestRefreshToken));
                })
                .orElseThrow(() -> new TokenRefreshException(requestRefreshToken,
                        "Refresh token is not in database!"));
    }

    @Override
    public ResponseEntity<?> logoutUser() {
        UserDetailsImpl userDetails = (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String userId = userDetails.getId().toString();
        refreshTokenService.deleteByUserId(userId);
        return ResponseEntity.ok(new MessageResponse("Log out successful!"));
    }
}
