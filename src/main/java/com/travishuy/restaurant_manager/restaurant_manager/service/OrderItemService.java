package com.travishuy.restaurant_manager.restaurant_manager.service;

import com.travishuy.restaurant_manager.restaurant_manager.model.OrderItem;
import com.travishuy.restaurant_manager.restaurant_manager.repository.OrderItemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
/**
 * Represents the service for the OrderItem.
 * This class is used to retrieve the order item.
 *
 * @version 0.1
 * @since 06-03-2025
 * @author TravisHuy
 */
@Service
public class OrderItemService {

    @Autowired
    OrderItemRepository orderItemRepository;

    public OrderItem getByOrderItemId(String orderItemId){
        try{
            if(orderItemId == null || orderItemId.trim().isEmpty()){
                throw new IllegalArgumentException("Order Item ID cannot be null or empty");
            }

            OrderItem orderItem = orderItemRepository.findById(orderItemId).orElseThrow(
                    () -> new IllegalArgumentException("Order Item not found"));
            return orderItem;
        }
        catch (IllegalArgumentException e) {
            throw e;
        } catch (Exception e) {
            throw new RuntimeException("Failed to retrieve order item with ID " + orderItemId + ": " + e.getMessage(), e);
        }
    }

    public List<OrderItem> getOrderItemByIds(List<String> orderItemIds){
        try{
            if(orderItemIds == null || orderItemIds.isEmpty()){
                throw new IllegalArgumentException("Order Item IDs list cannot be null or empty");
            }
            List<OrderItem> orderItems = orderItemRepository.findAllById(orderItemIds);
            if(orderItems.isEmpty()){
                throw new RuntimeException("No order items found for the provided IDs");
            }
            return orderItems;
        }
        catch (IllegalArgumentException e) {
            throw e;
        } catch (Exception e) {
            throw new RuntimeException("Failed to retrieve order items: " + e.getMessage(), e);
        }
    }
}
