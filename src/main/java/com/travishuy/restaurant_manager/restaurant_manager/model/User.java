package com.travishuy.restaurant_manager.restaurant_manager.model;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Represents a user entity in the system.
 * This class maps to the "users" collection in MongoDB database.
 *
 * @version 0.1
 * @since 03-02-2025
 * @author TravisHuy
 */
@Data
@Document(collection = "users")
public class User {
    /** Unique identifier for the user */
    @Id
    private String id;
    /** User's full name */
    private String name;
    /** User's email address */
    private String email;
    /** User's password */
    private String password;
    /** User's phone number for contact */
    private String phoneNumber;
    /** User's address */
    private String address;
    /** User's avatar image URL */
    private String avatar;
    /** User's role in the system */
    private Set<Role> role;
    /** User's authentication provider */
    private AuthProvider provider;
    /** User's provider id */
    private String providerId;

    public Set<String> getAuthorities() {
        if(role == null){
            return new HashSet<>();
        }
        return role.stream()
                .map(Role::name)
                .collect(Collectors.toSet());
    }
}
