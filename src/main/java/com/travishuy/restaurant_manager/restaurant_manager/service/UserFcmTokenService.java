package com.travishuy.restaurant_manager.restaurant_manager.service;

import com.travishuy.restaurant_manager.restaurant_manager.model.Role;
import com.travishuy.restaurant_manager.restaurant_manager.model.User;
import com.travishuy.restaurant_manager.restaurant_manager.model.UserFcmToken;
import com.travishuy.restaurant_manager.restaurant_manager.repository.UserFcmTokenRepository;
import com.travishuy.restaurant_manager.restaurant_manager.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.Set;

@Service
@Slf4j
public class UserFcmTokenService {
    @Autowired
    private UserFcmTokenRepository userFcmTokenRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private FirebaseMessagingService firebaseMessagingService;

    /**
     * Update FCM token for the authenticated user
     */
    public void updateFcmToken(String token) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            log.error("No authenticated user found");
            return;
        }

        String username = authentication.getName();
        Optional<User> userOpt = userRepository.findByEmail(username);

        if (userOpt.isEmpty()) {
            log.error("User not found: {}", username);
            return;
        }

        User user = userOpt.get();
        Set<Role> roles = user.getRole();

        UserFcmToken userFcmToken = userFcmTokenRepository.findById(user.getId())
                .orElse(new UserFcmToken());

        userFcmToken.setUserId(user.getId());
        userFcmToken.setFcmToken(token);
        userFcmToken.setRoles(roles);

        userFcmTokenRepository.save(userFcmToken);

        // Also register with Firebase Messaging Service for topic subscriptions
        firebaseMessagingService.registerToken(user.getId(), token, roles);

        log.info("Updated FCM token for user {}", user.getId());
    }
}
