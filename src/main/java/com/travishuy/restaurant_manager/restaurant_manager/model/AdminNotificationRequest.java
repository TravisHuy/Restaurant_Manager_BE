package com.travishuy.restaurant_manager.restaurant_manager.model;

import lombok.Data;

import java.util.Set;

/**
 * Request model for admin creating notifications
 *
 * @since 0.1
 * @author TravisHuy
 * @version 13-05--2025
 */
@Data
public class AdminNotificationRequest {
    private String title;
    private String message;
    private NotificationType type;
    private String relatedId;
    private Set<Role> recipientRoles;
}
