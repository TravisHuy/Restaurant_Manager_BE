package com.travishuy.restaurant_manager.restaurant_manager.repository;

import com.travishuy.restaurant_manager.restaurant_manager.model.Category;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
/**
 * Repository interface for managing category entities
 *
 * @version 0.1
 * @since 12-02-2025
 * @author TravisHuy
 */
@Repository
public interface CategoryRepository extends MongoRepository<Category, String> {
    /**
     * Check if a category with the given name exists in the database
     *
     * @param name the category name
     * @return true if the category exists, false otherwise
     */
    boolean existsByName(String name);
    /**
     * Check if a category with the given id exists in the database
     *
     * @param id the category id
     * @return true if the category exists, false otherwise
     */
    boolean existsById(String id);
}
