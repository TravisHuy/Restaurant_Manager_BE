package com.travishuy.restaurant_manager.restaurant_manager.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;


/**
 * Represents a notification for the admin in the system.
 * This class maps to the "notifications" collection in MongoDB database.
 *
 * @version 0.1
 * @since 16-04-2025
 * @author TravisHuy
 */
@Document(collection = "admin_notifications")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class AdminNotification {
    @Id
    private String id;
    private String title;
    private String message;
    private NotificationType type;
    private String relatedId;
    private boolean read;
    private LocalDateTime timestamp;
}
