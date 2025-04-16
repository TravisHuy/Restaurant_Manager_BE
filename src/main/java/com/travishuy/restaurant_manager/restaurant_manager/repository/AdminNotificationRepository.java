package com.travishuy.restaurant_manager.restaurant_manager.repository;

import com.travishuy.restaurant_manager.restaurant_manager.model.AdminNotification;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface AdminNotificationRepository extends MongoRepository<AdminNotification,String> {
    /**
     * Find all notifications by their read status.
     *
     * @param read the read status of the notifications
     * @return a list of notifications with the specified read status
     */
    List<AdminNotification> findByRead(boolean read);

    /**
     * Find all notifications by their type.
     *
     * @param type the type of the notifications
     * @return a list of notifications with the specified type
     */
    List<AdminNotification> findByType(String type);
}
