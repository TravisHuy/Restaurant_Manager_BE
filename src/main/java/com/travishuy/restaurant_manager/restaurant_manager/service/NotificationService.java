package com.travishuy.restaurant_manager.restaurant_manager.service;


import com.travishuy.restaurant_manager.restaurant_manager.model.Notification;
import com.travishuy.restaurant_manager.restaurant_manager.model.NotificationType;
import com.travishuy.restaurant_manager.restaurant_manager.model.Role;
import com.travishuy.restaurant_manager.restaurant_manager.repository.NotificationRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

/**
 * Service class for managing notification operations
 *
 * @version 0.1
 * @since 13-05-2025
 * @author TravisHuy
 */
@Service
@Slf4j
public class NotificationService {
    @Autowired
    private NotificationRepository notificationRepository;

    @Autowired
    private FirebaseMessagingService firebaseMessagingService;

    /**
     * Create a new notification in the database
     */
    public Notification createNotification(String title, String message, NotificationType type,
                                           String relatedId, Set<Role> recipientRoles) {
        Notification notification = new Notification();
        notification.setTitle(title);
        notification.setMessage(message);
        notification.setType(type);
        notification.setRelatedId(relatedId);
        notification.setRecipientRoles(recipientRoles);
        notification.setRead(false);
        notification.setTimestamp(LocalDateTime.now());

        log.info("Creating new notification: {}", notification);
        notification = notificationRepository.save(notification);

        // If this is a payment notification, also send FCM
        if (type == NotificationType.PAYMENT_RECEIVED) {
            firebaseMessagingService.sendNotification(title, message, type.name(), relatedId,
                    recipientRoles);
        }

        return notification;
    }


    /**
     * Get all notifications for a specific role
     */
    public List<Notification> getNotificationsForRole(Role role) {
        return notificationRepository.findByRecipientRolesContainingOrderByTimestampDesc(role);
    }

    /**
     * Get all unread notifications for a specific role
     */
    public List<Notification> getUnreadNotificationsForRole(Role role) {
        return notificationRepository.findByRecipientRolesContainingAndReadFalseOrderByTimestampDesc(role);
    }

    /**
     * Mark notification as read
     */
    public Notification markAsRead(String notificationId) {
        Notification notification = notificationRepository.findById(notificationId)
                .orElseThrow(() -> new RuntimeException("Notification not found with ID: " + notificationId));

        notification.setRead(true);
        return notificationRepository.save(notification);
    }

    /**
     * Mark all notifications for a role as read
     */
    public void markAllAsRead(Role role) {
        List<Notification> unreadNotifications = getUnreadNotificationsForRole(role);
        unreadNotifications.forEach(notification -> notification.setRead(true));
        notificationRepository.saveAll(unreadNotifications);
    }

    /**
     * Get notifications related to a specific entity (order, reservation, etc.)
     */
    public List<Notification> getNotificationsForEntity(String relatedId) {
        return notificationRepository.findByRelatedIdOrderByTimestampDesc(relatedId);
    }


    /**
     * Create and send a custom notification from admin
     */
    public Notification createAdminNotification(String title, String message,
                                                String relatedId, Set<Role> recipientRoles) {
        log.info("Creating admin notification: {} for roles: {}", title, recipientRoles);

        return createNotification(
                title,
                message,
                NotificationType.ADMIN_NOTIFICATION,
                relatedId,
                recipientRoles
        );
    }
    /**
     * Helper method to create a notification for a new reservation
     */
    public Notification notifyNewReservation(String tableId, String customerName, int numberOfPeople, String reservationId) {
        String title = "New Reservation";
        String message = String.format("Table #%s has been reserved by %s for %d people",
                tableId, customerName, numberOfPeople);

        return createNotification(
                title,
                message,
                NotificationType.NEW_RESERVATION,
                reservationId,
                Set.of(Role.ROLE_MANAGER, Role.ROLE_ADMIN)
        );
    }

    /**
     * Helper method to create a notification for a new order
     */
    public Notification notifyNewOrder(String orderId, String tableId, String customerName, double totalAmount) {
        String title = "New Order";
        String message = String.format("New order #%s from %s at Table #%s for %.2f VND",
                orderId, customerName, tableId, totalAmount);

        return createNotification(
                title,
                message,
                NotificationType.NEW_ORDER,
                orderId,
                Set.of(Role.ROLE_MANAGER, Role.ROLE_ADMIN, Role.ROLE_EMPLOYEE)
        );
    }

    /**
     * Helper method to create a notification for a payment
     */
    public Notification notifyPaymentReceived(String orderId, String customerName, double amount) {
        String title = "Payment Received";
        String message = String.format("Payment of %.2f VND received for order #%s from %s",
                amount, orderId, customerName);

        return createNotification(
                title,
                message,
                NotificationType.PAYMENT_RECEIVED,
                orderId,
                Set.of(Role.ROLE_MANAGER, Role.ROLE_ADMIN)
        );
    }

    /**
     * Helper method to create a notification for order status change
     */
    public Notification notifyOrderStatusChange(String orderId, String customerName, String status) {
        String title = "Order Status Changed";
        String message = String.format("Order #%s for %s is now %s", orderId, customerName, status);

        return createNotification(
                title,
                message,
                NotificationType.ORDER_STATUS_CHANGE,
                orderId,
                Set.of(Role.ROLE_MANAGER, Role.ROLE_ADMIN, Role.ROLE_EMPLOYEE)
        );
    }
}
