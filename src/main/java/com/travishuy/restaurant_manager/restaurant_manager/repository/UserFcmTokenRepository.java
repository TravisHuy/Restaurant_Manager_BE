package com.travishuy.restaurant_manager.restaurant_manager.repository;

import com.travishuy.restaurant_manager.restaurant_manager.model.Role;
import com.travishuy.restaurant_manager.restaurant_manager.model.UserFcmToken;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserFcmTokenRepository extends MongoRepository<UserFcmToken,String> {
    List<UserFcmToken> findByRolesContaining(Role role);
}
