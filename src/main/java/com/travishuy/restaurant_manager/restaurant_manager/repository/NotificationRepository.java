package com.travishuy.restaurant_manager.restaurant_manager.repository;

import com.travishuy.restaurant_manager.restaurant_manager.model.Notification;
import com.travishuy.restaurant_manager.restaurant_manager.model.NotificationType;
import com.travishuy.restaurant_manager.restaurant_manager.model.Role;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Repository interface for Notification entities
 *
 * @version 0.1
 * @since 16-04-2025
 * @author TravisHuy
 */
@Repository
public interface NotificationRepository extends MongoRepository<Notification, String> {

    /**
     * Find all notifications for a specific role
     */
    List<Notification> findByRecipientRolesContainingOrderByTimestampDesc(Role role);

    /**
     * Find all notifications for a specific role that haven't been read
     */
    List<Notification> findByRecipientRolesContainingAndReadFalseOrderByTimestampDesc(Role role);

    /**
     * Find all notifications by type
     */
    List<Notification> findByTypeOrderByTimestampDesc(NotificationType type);

    /**
     * Find notifications related to a specific entity (order, reservation, etc.)
     */
    List<Notification> findByRelatedIdOrderByTimestampDesc(String relatedId);
}