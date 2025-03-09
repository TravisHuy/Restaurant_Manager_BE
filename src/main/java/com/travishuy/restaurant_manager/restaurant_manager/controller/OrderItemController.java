package com.travishuy.restaurant_manager.restaurant_manager.controller;

import com.travishuy.restaurant_manager.restaurant_manager.dto.OrderItemDTO;
import com.travishuy.restaurant_manager.restaurant_manager.model.OrderItem;
import com.travishuy.restaurant_manager.restaurant_manager.service.OrderItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controller class for handling order item requests
 *
 * @version 0.1
 * @since 06-03--2025
 * @author TravisHuy
 */
@RequestMapping("/api/orderItems")
@RestController
public class OrderItemController {
    @Autowired
    OrderItemService orderItemService;

    @GetMapping("/getOrderItemById/{orderItemId}")
    public ResponseEntity<?> getOrderItemById(@PathVariable String orderItemId){
        try {
            OrderItemDTO orderItem = orderItemService.getByOrderItemId(orderItemId);
            return ResponseEntity.ok(orderItem);
        }
        catch (RuntimeException e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
    @GetMapping("/getOrderItemsByIds")
    public ResponseEntity<?> getListOrderItem(@RequestParam List<String> orderItemIds){
        try {
            List<OrderItemDTO> orderItems = orderItemService.getOrderItemByIds(orderItemIds);
            return ResponseEntity.ok(orderItems);
        }
        catch (RuntimeException e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
