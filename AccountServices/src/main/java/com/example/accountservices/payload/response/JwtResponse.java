package com.example.accountservices.payload.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class JwtResponse {
    private String token;
    private String refreshToken;
    private String id;
    private String email;
    private String role;
}
