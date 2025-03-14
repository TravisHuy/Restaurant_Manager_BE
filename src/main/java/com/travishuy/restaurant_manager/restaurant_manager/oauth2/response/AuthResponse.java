package com.travishuy.restaurant_manager.restaurant_manager.oauth2.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
/**
 * Represents a response to the authentication request.
 * This class is used to map the response body to a Java object.
 *
 * @version 0.1
 * @since 04-02-2025
 * @author TravisHuy
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class AuthResponse {
    /** The token of the user */
    private String token;
    /** The message of the response */
    private String message;
    /** The type of the token */
    private String type = "Bearer";
    /** The refresh token of the user */
    private String refreshToken;
    /** The email of the user */
    private String email;
    /** The role of the user */
    private String role;
    /** The name of the user */
    private String name;
    /** The id of the user */
    private String id;
    /** The isAdmin of the user */
    private boolean isAdmin;

    /**
     * Constructs a new AuthResponse with the given parameters.
     *
     * @param message The message of the response
     * @param token The token of the user
     */
    public AuthResponse(String message, String token) {
        this.message = message;
        this.token = token;
    }

    /**
     * Constructs a new AuthResponse with the given parameters.
     *
     * @param token The token of the user
     * @param refreshToken The refresh token of the user
     * @param message The message of the response
     * @param email The email of the user
     * @param role The role of the user
     * @param name The name of the user
     * @param id The id of the user
     */
    public AuthResponse(String token, String refreshToken, String message, String email, String role, String name, String id,boolean isAdmin) {
        this.token = token;
        this.refreshToken = refreshToken;
        this.message = message;
        this.email = email;
        this.role = role;
        this.name = name;
        this.id = id;
        this.isAdmin = isAdmin;
    }


}
