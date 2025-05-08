package com.travishuy.restaurant_manager.restaurant_manager.controller;

import com.travishuy.restaurant_manager.restaurant_manager.model.AdminNotification;
import com.travishuy.restaurant_manager.restaurant_manager.service.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/notifications")
public class NotificationController {

    @Autowired
    private NotificationService notificationService;

    @GetMapping("/all")
    public List<AdminNotification> getAllNotifications() {
        return notificationService.getAllNotifications();
    }

    @GetMapping("/unread")
    public List<AdminNotification> getUnreadNotifications() {
        return notificationService.getUnreadNotifications();
    }

    @PostMapping("/{id}/mark-read")
    public AdminNotification markAsRead(@PathVariable("id") String notificationId) {
        return notificationService.markAsRead(notificationId);
    }
}
