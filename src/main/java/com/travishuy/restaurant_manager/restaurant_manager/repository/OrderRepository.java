package com.travishuy.restaurant_manager.restaurant_manager.repository;

import com.travishuy.restaurant_manager.restaurant_manager.model.Order;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

/**
 * Repository interface for managing order entities
 *
 * @version 0.1
 * @since 18-02-2025
 * @author TravisHuy
 */
@Repository
public interface OrderRepository extends MongoRepository<Order,String> {
}
