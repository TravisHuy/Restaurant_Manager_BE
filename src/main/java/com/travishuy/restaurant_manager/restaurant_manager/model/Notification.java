package com.travishuy.restaurant_manager.restaurant_manager.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.Set;

/**
 * Represents a notification in the system.
 * This class maps to the "notifications" collection in MongoDB database.
 *
 * @version 0.1
 * @since 16-04-2025
 * @author TravisHuy
 */
@Document(collection = "notifications")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Notification {
    /** Unique identifier for the notification */
    @Id
    private String id;

    /** Title of the notification */
    private String title;

    /** Detailed message of the notification */
    private String message;

    /** Type of notification */
    private NotificationType type;

    /** ID related to this notification (orderId, reservationId, etc.) */
    private String relatedId;

    /** Whether this notification has been read */
    private boolean read;

    /** When this notification was created */
    private LocalDateTime timestamp;

    /** The recipient roles that should see this notification */
    private Set<Role> recipientRoles;
}