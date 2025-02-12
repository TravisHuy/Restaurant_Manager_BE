package com.travishuy.restaurant_manager.restaurant_manager.repository;

import com.travishuy.restaurant_manager.restaurant_manager.model.Table;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

/**
 * Repository interface for managing Table entities
 *
 * @version 0.1
 * @since 12-02-2025
 * @author TravisHuy
 */
@Repository
public interface TableRepository extends MongoRepository<Table, String> {
    /**
     * Check if a table with the given number exists in the database
     *
     * @param number the table number
     * @return true if the table exists, false otherwise
     */
    boolean existsByNumber(int number);
}
