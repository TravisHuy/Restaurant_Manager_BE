package com.travishuy.restaurant_manager.restaurant_manager.oauth2.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AuthResponse {
    private String token;
    private String message;
    private String type = "Bearer";
    private String refreshToken;
    private String email;
    private String role;
    private String name;
    private String id;

    public AuthResponse(String message, String token) {
        this.message = message;
        this.token = token;
    }
}
