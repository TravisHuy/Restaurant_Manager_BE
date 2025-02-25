package com.travishuy.restaurant_manager.restaurant_manager.repository;

import com.travishuy.restaurant_manager.restaurant_manager.model.OrderItem;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
/**
 * Repository interface for managing OrderItemRepository entities
 *
 * @version 0.1
 * @since 24-02-2025
 * @author TravisHuy
 */
@Repository
public interface OrderItemRepository extends MongoRepository<OrderItem,String> {

}
