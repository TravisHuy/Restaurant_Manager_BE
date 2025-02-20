package com.travishuy.restaurant_manager.restaurant_manager.repository;

import com.travishuy.restaurant_manager.restaurant_manager.model.MenuItem;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

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

    /**
     * Check if a menuItem with the given name exists in the database
     *
     * @param name
     * @return
     */
    boolean existsByName(String name);

    /**
     * Find all menu items by category id
     *
     * @param categoryId the category id
     * @return a list of menu items
     */
    List<MenuItem> findByCategoryId(String categoryId);
}
