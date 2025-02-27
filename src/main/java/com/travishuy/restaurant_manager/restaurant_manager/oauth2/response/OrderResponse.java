package com.travishuy.restaurant_manager.restaurant_manager.oauth2.response;

import com.travishuy.restaurant_manager.restaurant_manager.dto.OrderItemDTO;
import com.travishuy.restaurant_manager.restaurant_manager.model.Status;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class OrderResponse {
    private String id;
    private String customerName;
    private String tableId;
    private List<OrderItemDTO> items;
    private LocalDateTime orderTime;
    private Status status;
    private double totalAmount;
}
