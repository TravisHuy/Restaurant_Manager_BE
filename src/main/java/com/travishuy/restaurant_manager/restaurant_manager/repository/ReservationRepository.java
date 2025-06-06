package com.travishuy.restaurant_manager.restaurant_manager.repository;

import com.travishuy.restaurant_manager.restaurant_manager.model.Reservation;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
/**
 * Repository interface for managing reservation entities
 *
 * @version 0.1
 * @since 28-02-2025
 * @author TravisHuy
 */
@Repository
public interface ReservationRepository extends MongoRepository<Reservation,String> {
    boolean existsByTableId(String tableId);
}
