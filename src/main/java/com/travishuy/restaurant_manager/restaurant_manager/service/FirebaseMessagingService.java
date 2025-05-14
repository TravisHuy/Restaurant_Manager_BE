package com.travishuy.restaurant_manager.restaurant_manager.service;


import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.messaging.*;
import com.travishuy.restaurant_manager.restaurant_manager.model.Role;
import com.travishuy.restaurant_manager.restaurant_manager.model.UserFcmToken;
import com.travishuy.restaurant_manager.restaurant_manager.repository.UserFcmTokenRepository;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ExecutionException;

/**
 * Service class for handling Firebase Cloud Messaging operations
 *
 * @author TravisHuy
 * @version 0.1
 * @since 13-05-2025
 */
@Service
@Slf4j
public class FirebaseMessagingService {

    @Value("${firebase.config.path}")
    private Resource firebaseConfig;

    @Value("${firebase.topic.admin}")
    private String adminTopic;

    @Value("${firebase.topic.manager}")
    private String managerTopic;

    @Value("${firebase.topic.employee}")
    private String employeeTopic;

    @Autowired
    private UserFcmTokenRepository userFcmTokenRepository;


    @PostConstruct
    public void init() {
        try {
            InputStream serviceAccount = firebaseConfig.getInputStream();
            FirebaseOptions options = new FirebaseOptions.Builder()
                    .setCredentials(GoogleCredentials.fromStream(serviceAccount))
                    .build();
            FirebaseApp.initializeApp(options);

            if (FirebaseApp.getApps().isEmpty()) {
                FirebaseApp.initializeApp(options);
                log.info("Firebase application has been initialized");
            }
        } catch (Exception e) {
            log.error("Error initializing Firebase Messaging Service: {}", e.getMessage());
        }
    }


    /**
     * Send a notification to specific roles via Firebase Cloud Messaging
     */
    public void sendNotification(String title, String body, String type, String relatedId, Set<Role> roles) {
        roles.forEach(role -> {
            String topic = getTopic(role);

            Map<String, String> data = new HashMap<>();
            data.put("type", type);
            data.put("relatedId", relatedId);

            Message message = Message.builder()
                    .setNotification(Notification.builder()
                            .setTitle(title)
                            .setBody(body)
                            .build())
                    .putAllData(data)
                    .setTopic(topic)
                    .build();

            try {
                String response = FirebaseMessaging.getInstance().send(message);
                log.info("Notification sent to topic {} with response: {}", topic, response);
            } catch (FirebaseMessagingException e) {
                log.error("Error sending notification to topic {}", topic, e);
            }
        });
    }

    /**
     * Get the appropriate Firebase topic for a given role
     */
    private String getTopic(Role role) {
        return switch (role) {
            case ROLE_ADMIN -> adminTopic;
            case ROLE_MANAGER -> managerTopic;
            case ROLE_EMPLOYEE -> employeeTopic;
            default -> throw new IllegalArgumentException("Unknown role: " + role);
        };
    }

    /**
     * Send notifications directly to user devices based on FCM token
     */
    private void sendDirectNotification(String title, String body, String type, String relateId, Set<Role> roles) {
        roles.forEach(
                role -> {
                    List<UserFcmToken> userTokens = userFcmTokenRepository.findByRolesContaining(role);

                    if (userTokens.isEmpty()) {
                        log.info("No FCM tokens found for role: {}", role);
                        return;
                    }

                    Map<String, String> data = new HashMap<>();
                    data.put("type", type);
                    data.put("relateId", relateId);

                    MulticastMessage multicastMessage = MulticastMessage.builder()
                            .setNotification(Notification.builder().setTitle(title).setBody(body).build()).putAllData(data)
                            .addAllTokens(userTokens.stream().map(UserFcmToken::getFcmToken).toList()).build();
                    try {
                        BatchResponse response = FirebaseMessaging.getInstance().sendMulticast(multicastMessage);
                        log.info("Sent message to {} recipients with {} failures", response.getSuccessCount(), response.getFailureCount());

                        // Handle failed messages
                        if (response.getFailureCount() > 0) {
                            List<SendResponse> responses = response.getResponses();
                            for (int i = 0; i < responses.size(); i++) {
                                if (!responses.get(i).isSuccessful()) {
                                    log.error("Failed to send message to token: {}", userTokens.get(i).getFcmToken());
                                }
                            }
                        }
                    } catch (FirebaseMessagingException e) {
                        log.error("Error sending notifications to users with role {}", role, e);
                    }
                }
        );
    }

    /**
     * Register FCM Token for a user
     */
    public void registerToken(String userId,String token,Set<Role> roles){
        UserFcmToken userFcmToken = new UserFcmToken();
        userFcmToken.setFcmToken(token);
        userFcmToken.setUserId(userId);
        userFcmToken.setRoles(roles);

        userFcmTokenRepository.save(userFcmToken);
        log.info("Registered FCM token for user {}", userId);

        // Subscribe to appropriate topics based on roles
        roles.forEach(role -> {
            String topic = getTopic(role);
            try {
                TopicManagementResponse response = FirebaseMessaging.getInstance()
                        .subscribeToTopicAsync(List.of(token), topic).get();
                log.info("Subscribed token to topic {} with {} successes and {} failures",
                        topic, response.getSuccessCount(), response.getFailureCount());
            } catch (InterruptedException | ExecutionException e) {
                log.error("Error subscribing token to topic {}", topic, e);
                Thread.currentThread().interrupt();
            }
        });
    }
}
