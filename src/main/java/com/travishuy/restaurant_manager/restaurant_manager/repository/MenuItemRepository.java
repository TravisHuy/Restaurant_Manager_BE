package com.travishuy.restaurant_manager.restaurant_manager.repository;

import com.travishuy.restaurant_manager.restaurant_manager.model.MenuItem;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
/**
 * Repository interface for managing menuItem entities
 *
 * @version 0.1
 * @since 12-02-2025
 * @author TravisHuy
 */
@Repository
public interface MenuItemRepository extends MongoRepository<MenuItem,String> {
    /**
     * Check if a menuItem with the given id exists in the database
     *
     * @param id the menuItem id
     * @return true if the menuItem exists, false otherwise
     */
    boolean existsById(String id);
}
