package com.travishuy.restaurant_manager.restaurant_manager.service;

import com.travishuy.restaurant_manager.restaurant_manager.model.AdminNotification;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

/**
 *  Notification Consumer Service
 *
 * @version 0.1
 * @author travishuy
 * @since  16/04/2025
 *
 */

@Service
public class NotificationConsumerService {

    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    @KafkaListener(topics = "admin-notifications", groupId = "restaurant-group")
    public void consumeNotification(AdminNotification notification){
        messagingTemplate.convertAndSend("/topic/admin-notifications",notification);
    }
}
