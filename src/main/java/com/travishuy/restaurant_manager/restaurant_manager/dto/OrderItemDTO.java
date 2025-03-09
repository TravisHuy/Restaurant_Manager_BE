package com.travishuy.restaurant_manager.restaurant_manager.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderItemDTO {
    private String id;
    private List<MenuDTO> menuItems;
    private double totalPrice;
    private String note;
}

