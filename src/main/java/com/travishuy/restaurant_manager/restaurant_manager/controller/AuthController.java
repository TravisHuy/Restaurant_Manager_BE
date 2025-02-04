package com.travishuy.restaurant_manager.restaurant_manager.controller;

import com.travishuy.restaurant_manager.restaurant_manager.oauth2.request.LoginRequest;
import com.travishuy.restaurant_manager.restaurant_manager.oauth2.request.SignUpRequest;
import com.travishuy.restaurant_manager.restaurant_manager.oauth2.response.AuthResponse;
import com.travishuy.restaurant_manager.restaurant_manager.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;

    @PostMapping("/signup")
    public ResponseEntity<AuthResponse> registerUser(@RequestBody SignUpRequest signUpRequest){
        return ResponseEntity.ok(authService.registerUser(signUpRequest));
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> authenticateUser(@RequestBody LoginRequest loginRequest){
        return ResponseEntity.ok(authService.authenticateUser(loginRequest));
    }

    @GetMapping("/oauth2/callback/{provider}")
    public ResponseEntity<AuthResponse> oauth2Callback(@PathVariable String provider, @RequestParam String code){
        return ResponseEntity.ok(authService.processOAuth2Login(provider, code));
    }
}
