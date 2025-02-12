package com.travishuy.restaurant_manager.restaurant_manager.controller;

import com.travishuy.restaurant_manager.restaurant_manager.oauth2.request.LoginRequest;
import com.travishuy.restaurant_manager.restaurant_manager.oauth2.request.SignUpRequest;
import com.travishuy.restaurant_manager.restaurant_manager.oauth2.response.AuthResponse;
import com.travishuy.restaurant_manager.restaurant_manager.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
/**
 * Controller class for handling authentication requests
 *
 * @version 0.1
 * @since 04-02-2025
 * @author TravisHuy
 */
@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    /** Service class for handling authentication operations */
    private final AuthService authService;
    /**
     * Returns the status of the authentication service
     *
     * @return ResponseEntity with the status message
     */
    @GetMapping("/status")
    public ResponseEntity<?> getStatus() {
        return ResponseEntity.ok("Auth service is running");
    }
    /**
     * Registers a new user in the system
     *
     * @param signUpRequest the request object containing the user details
     * @return ResponseEntity with the authentication response
     */
    @PostMapping("/signup")
    public ResponseEntity<AuthResponse> registerUser(@RequestBody SignUpRequest signUpRequest){
        return ResponseEntity.ok(authService.registerUser(signUpRequest));
    }
    /**
     * Authenticates a user in the system
     *
     * @param loginRequest the request object containing the user credentials
     * @return ResponseEntity with the authentication response
     */
    @PostMapping("/login")
    public ResponseEntity<AuthResponse> authenticateUser(@RequestBody LoginRequest loginRequest){
        return ResponseEntity.ok(authService.authenticateUser(loginRequest));
    }
    /**
     * Authenticates a user using OAuth2 provider
     *
     * @param provider the OAuth2 provider name
     * @param code the authorization code
     * @return ResponseEntity with the authentication response
     */
    @GetMapping("/oauth2/callback/{provider}")
    public ResponseEntity<AuthResponse> oauth2Callback(@PathVariable String provider, @RequestParam String code){
        return ResponseEntity.ok(authService.processOAuth2Login(provider, code));
    }
}
