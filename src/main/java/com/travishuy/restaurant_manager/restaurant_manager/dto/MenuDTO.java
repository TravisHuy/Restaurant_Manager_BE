package com.travishuy.restaurant_manager.restaurant_manager.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MenuDTO {
    private String menuItemId;
    private String menuItemName;
    private int quantity;
    private double price;
}
