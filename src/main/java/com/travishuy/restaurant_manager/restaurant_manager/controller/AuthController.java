package com.travishuy.restaurant_manager.restaurant_manager.controller;

import com.travishuy.restaurant_manager.restaurant_manager.oauth2.request.LoginRequest;
import com.travishuy.restaurant_manager.restaurant_manager.oauth2.request.SignUpRequest;
import com.travishuy.restaurant_manager.restaurant_manager.oauth2.response.AuthResponse;
import com.travishuy.restaurant_manager.restaurant_manager.service.AuthService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
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
        AuthResponse authResponse = authService.authenticateUser(loginRequest);
        System.out.println("Generated JWT: " + authResponse.getToken()); // Kiểm tra JWT
        return ResponseEntity.ok(authResponse);
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

    @PostMapping("/logout")
    public ResponseEntity<?> logoutUser(HttpServletRequest request){
        try{
            String token = request.getHeader("Authorization");
            return ResponseEntity.ok(authService.logoutUser(token));
        }
        catch (RuntimeException e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    /**
     * Authenticates an admin in the system
     *
     * @param loginRequest the request object containing the admin credentials
     * @return ResponseEntity with the authentication response
     */
    @PostMapping("/admin/auth")
    public ResponseEntity<?> authenticateAdmin(@RequestBody LoginRequest loginRequest){
        try{
            AuthResponse authResponse = authService.authenticateAdmin(loginRequest);
            return ResponseEntity.ok(authResponse);
        }
        catch (RuntimeException e){
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(new AuthResponse(null, null, e.getMessage(), null, null, null, null,true));
        }
    }

    @PostMapping("/admin/signup")
    public ResponseEntity<?> registerAdmin(@RequestBody SignUpRequest signUpRequest){
        try{
            AuthResponse authResponse = authService.registerAdmin(signUpRequest);
            return ResponseEntity.ok(authResponse);
        }
        catch (RuntimeException e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
