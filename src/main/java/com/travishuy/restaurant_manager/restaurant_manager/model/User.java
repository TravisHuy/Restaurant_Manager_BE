package com.travishuy.restaurant_manager.restaurant_manager.model;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Set;


@Data
@Document(collection = "users")
public class User {
    @Id
    private String id;
    private String name;
    private String phoneNumber;
    private String address;
    private String avatar;
    private String role;
    private AuthProvider provider;
    private String providerId;
    private Set<String> authorities;
}
