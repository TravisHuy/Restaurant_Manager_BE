package com.travishuy.restaurant_manager.restaurant_manager.controller;

import com.travishuy.restaurant_manager.restaurant_manager.model.AdminNotification;
import com.travishuy.restaurant_manager.restaurant_manager.service.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

import java.util.List;

//@Controller
//public class WebSocketController {
//
//    @Autowired
//    private NotificationService notificationService;
//
//    @MessageMapping("/notifications/mark-read")
//    public void markNotificationAsRead(String notificationId) {
//        notificationService.markAsRead(notificationId);
//    }
//
//    @MessageMapping("/notifications/get-unread")
//    @SendTo("/topic/unread-notifications")
//    public List<AdminNotification> getUnreadNotifications() {
//        return notificationService.getUnreadNotifications();
//    }
//
//    @MessageMapping("/notifications/all")
//    @SendTo("/topic/all-notifications")
//    public List<AdminNotification> getAllNotifications() {
//        return notificationService.getAllNotifications();
//    }
//}
