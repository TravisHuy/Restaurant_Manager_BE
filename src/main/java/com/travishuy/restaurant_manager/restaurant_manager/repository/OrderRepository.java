package com.travishuy.restaurant_manager.restaurant_manager.repository;

import com.travishuy.restaurant_manager.restaurant_manager.model.Order;
import com.travishuy.restaurant_manager.restaurant_manager.model.Status;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import javax.swing.text.html.Option;
import java.util.List;
import java.util.Optional;

/**
 * Repository interface for managing order entities
 *
 * @version 0.1
 * @since 18-02-2025
 * @author TravisHuy
 */
@Repository
public interface OrderRepository extends MongoRepository<Order,String> {
    List<Order> findByStatus(Status status);
    Optional<Order> findByTableId(String tableId);
}
