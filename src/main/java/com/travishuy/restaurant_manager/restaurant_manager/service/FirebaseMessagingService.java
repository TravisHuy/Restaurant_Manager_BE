package com.travishuy.restaurant_manager.restaurant_manager.service;


import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingException;
import com.google.firebase.messaging.Message;
import com.google.firebase.messaging.Notification;
import com.travishuy.restaurant_manager.restaurant_manager.model.Role;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * Service class for handling Firebase Cloud Messaging operations
 *
 * @version 0.1
 * @since 13-05-2025
 * @author TravisHuy
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


    @PostConstruct
    public void init(){
        try{
            InputStream serviceAccount = firebaseConfig.getInputStream();
            FirebaseOptions options = new FirebaseOptions.Builder()
                    .setCredentials(GoogleCredentials.fromStream(serviceAccount))
                    .build();
            FirebaseApp.initializeApp(options);

            if (FirebaseApp.getApps().isEmpty()) {
                FirebaseApp.initializeApp(options);
                log.info("Firebase application has been initialized");
            }
        }
        catch (Exception e){
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
        switch (role) {
            case ROLE_ADMIN:
                return adminTopic;
            case ROLE_MANAGER:
                return managerTopic;
            case ROLE_EMPLOYEE:
                return employeeTopic;
            default:
                throw new IllegalArgumentException("Unknown role: " + role);
        }
    }
}
