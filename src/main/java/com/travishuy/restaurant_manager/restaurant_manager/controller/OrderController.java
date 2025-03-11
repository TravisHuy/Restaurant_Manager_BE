package com.travishuy.restaurant_manager.restaurant_manager.controller;

import com.travishuy.restaurant_manager.restaurant_manager.model.Order;
import com.travishuy.restaurant_manager.restaurant_manager.model.OrderItem;
import com.travishuy.restaurant_manager.restaurant_manager.oauth2.request.OrderItemRequest;
import com.travishuy.restaurant_manager.restaurant_manager.oauth2.request.OrderRequest;
import com.travishuy.restaurant_manager.restaurant_manager.oauth2.response.OrderResponse;
import com.travishuy.restaurant_manager.restaurant_manager.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controller class for handling order requests
 *
 * @version 0.1
 * @since 24-02-2025
 * @author TravisHuy
 */
@RestController
@RequestMapping("/api/orders")
public class OrderController {

    @Autowired
    private OrderService orderService;

    @PostMapping("/add")
    public ResponseEntity<OrderResponse> createOrder(@RequestBody OrderRequest orderRequest) {
        OrderResponse createdOrder = orderService.createOrder(orderRequest);
        return new ResponseEntity<>(createdOrder, HttpStatus.CREATED);
    }
    @GetMapping("/customerName/{tableId}")
    public ResponseEntity<String> getCustomerNameForTable(@PathVariable String tableId){
        return orderService.getCustomerName(tableId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
    @PostMapping("/addItems/{orderId}")
    public ResponseEntity<?> addItemToOrder(
            @PathVariable String orderId,
            @RequestBody List<OrderItemRequest> newItems){
        try{
            OrderResponse response = orderService.addItemsOrder(orderId, newItems);
            return ResponseEntity.ok(response);
        }
        catch (RuntimeException e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
    @GetMapping("/all")
    public ResponseEntity<?> getAllOrders(){
        try{
            List<Order> response = orderService.getAllOrders();
            return ResponseEntity.ok(response);
        }
        catch (RuntimeException e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
    @GetMapping("/{orderId}")
    public ResponseEntity<?> getOrderById(@PathVariable String orderId){
        try{
            Order response = orderService.getOrderId(orderId);
            return ResponseEntity.ok(response);
        }
        catch (RuntimeException e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
    @PostMapping("/updateStatus/{orderId}")
    public ResponseEntity<?> updateOrderStatus(@PathVariable String orderId){
        try{
            OrderResponse response = orderService.updateOrderStatus(orderId);
            return ResponseEntity.ok(response);
        }
        catch (RuntimeException e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}