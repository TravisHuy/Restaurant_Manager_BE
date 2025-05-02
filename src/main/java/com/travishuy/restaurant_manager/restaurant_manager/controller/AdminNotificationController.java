package com.travishuy.restaurant_manager.restaurant_manager.controller;

import com.travishuy.restaurant_manager.restaurant_manager.model.AdminNotification;
import com.travishuy.restaurant_manager.restaurant_manager.repository.AdminNotificationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin/notifications")
public class AdminNotificationController {

    @Autowired
    AdminNotificationRepository adminNotificationRepository;

    @GetMapping("/all")
    public ResponseEntity<?> getAllNotifications(){
        List<AdminNotification> notifications = adminNotificationRepository.findAll();
        return ResponseEntity.ok(notifications);
    }
    @GetMapping("/unread")
    public ResponseEntity<List<AdminNotification>> getUnreadNotifications() {
        List<AdminNotification> unreadNotifications = adminNotificationRepository.findByReadFalse();
        return ResponseEntity.ok(unreadNotifications);
    }

    @PutMapping("/{id}/read")
    public ResponseEntity<?> markAsRead(@PathVariable String id) {
        AdminNotification notification = adminNotificationRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Notification not found"));

        notification.setRead(true);
        adminNotificationRepository.save(notification);

        return ResponseEntity.ok().build();
    }
}
