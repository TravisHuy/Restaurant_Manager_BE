package com.travishuy.restaurant_manager.restaurant_manager.controller;

import com.travishuy.restaurant_manager.restaurant_manager.model.AdminNotification;
import com.travishuy.restaurant_manager.restaurant_manager.model.Notification;
import com.travishuy.restaurant_manager.restaurant_manager.model.Role;
import com.travishuy.restaurant_manager.restaurant_manager.service.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/notifications")
public class NotificationController {

//
//    @GetMapping("/all")
//    public List<AdminNotification> getAllNotifications() {
//        return notificationService.getAllNotifications();
//    }
//
//    @GetMapping("/unread")
//    public List<AdminNotification> getUnreadNotifications() {
//        return notificationService.getUnreadNotifications();
//    }
//
//    @PostMapping("/{id}/mark-read")
//    public AdminNotification markAsRead(@PathVariable("id") String notificationId) {
//        return notificationService.markAsRead(notificationId);
//    }

    @Autowired
    private NotificationService notificationService;

    /**
     * Get all notifications for admin role
     */
    @GetMapping("/admin")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<Notification>> getAdminNotifications() {
        return ResponseEntity.ok(notificationService.getNotificationsForRole(Role.ROLE_ADMIN));
    }

    /**
     * Get all notifications for manager role
     */
    @GetMapping("/manager")
    @PreAuthorize("hasRole('MANAGER')")
    public ResponseEntity<List<Notification>> getManagerNotifications() {
        return ResponseEntity.ok(notificationService.getNotificationsForRole(Role.ROLE_MANAGER));
    }

    /**
     * Get all notifications for employee role
     */
    @GetMapping("/employee")
    @PreAuthorize("hasRole('EMPLOYEE')")
    public ResponseEntity<List<Notification>> getEmployeeNotifications() {
        return ResponseEntity.ok(notificationService.getNotificationsForRole(Role.ROLE_EMPLOYEE));
    }

    /**
     * Get unread notifications for a role
     */
    @GetMapping("/unread/{role}")
    @PreAuthorize("hasRole(#role)")
    public ResponseEntity<List<Notification>> getUnreadNotifications(@PathVariable String role) {
        Role roleEnum = Role.valueOf("ROLE_" + role.toUpperCase());
        return ResponseEntity.ok(notificationService.getUnreadNotificationsForRole(roleEnum));
    }

    /**
     * Mark a notification as read
     */
    @PutMapping("/{notificationId}/read")
    public ResponseEntity<Notification> markAsRead(@PathVariable String notificationId) {
        return ResponseEntity.ok(notificationService.markAsRead(notificationId));
    }

    /**
     * Mark all notifications for a role as read
     */
    @PutMapping("/read-all/{role}")
    @PreAuthorize("hasRole(#role)")
    public ResponseEntity<Void> markAllAsRead(@PathVariable String role) {
        Role roleEnum = Role.valueOf("ROLE_" + role.toUpperCase());
        notificationService.markAllAsRead(roleEnum);
        return ResponseEntity.ok().build();
    }

    /**
     * Get notifications for a specific entity
     */
    @GetMapping("/entity/{entityId}")
    public ResponseEntity<List<Notification>> getNotificationsForEntity(@PathVariable String entityId) {
        return ResponseEntity.ok(notificationService.getNotificationsForEntity(entityId));
    }
}
