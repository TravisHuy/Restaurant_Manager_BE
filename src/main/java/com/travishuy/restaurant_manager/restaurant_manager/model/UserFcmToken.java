package com.travishuy.restaurant_manager.restaurant_manager.model;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Set;

@Document(collection = "user_fcm_token")
@Data
public class UserFcmToken {
    @Id
    private String userId;
    private String fcmToken;
    private Set<Role> roles;
}
