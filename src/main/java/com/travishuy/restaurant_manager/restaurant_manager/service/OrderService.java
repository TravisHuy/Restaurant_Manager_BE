package com.travishuy.restaurant_manager.restaurant_manager.service;

import com.travishuy.restaurant_manager.restaurant_manager.model.Order;
import com.travishuy.restaurant_manager.restaurant_manager.model.OrderItem;
import com.travishuy.restaurant_manager.restaurant_manager.model.Status;
import com.travishuy.restaurant_manager.restaurant_manager.oauth2.request.OrderRequest;
import com.travishuy.restaurant_manager.restaurant_manager.oauth2.response.OrderResponse;
import com.travishuy.restaurant_manager.restaurant_manager.repository.OrderItemRepository;
import com.travishuy.restaurant_manager.restaurant_manager.repository.OrderRepository;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Represents the service for the Order.
 * This interface is used to create the order.
 *
 * @version 0.1
 * @since 04-02-2025
 * @author TravisHuy
 */
@Service
public interface OrderService {
    OrderResponse  createOrder(OrderRequest orderRequest);
}