package com.travishuy.restaurant_manager.restaurant_manager.repository;

import com.travishuy.restaurant_manager.restaurant_manager.model.Table;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

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

    /**
     * Find tables by availability status
     * @param available availability status
     * @return List of tables with specified availability
     */
    List<Table> findByAvailable(boolean available);

    /**
     * Find tables by floor ID
     * @param floorId the ID of the floor
     * @return List of tables on the specified floor
     */
    List<Table> findByFloorId(String floorId);

    /**
     * Find tables by capacity greater than or equal to specified value
     * @param capacity minimum capacity required
     * @return List of tables with sufficient capacity
     */
    List<Table> findByCapacityGreaterThanEqual(int capacity);

    /**
     * Find a table by its associated order ID
     * @param orderId the ID of the order
     * @return the table associated with the order
     */
    Table findByOrderId(String orderId);
}
