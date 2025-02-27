package com.travishuy.restaurant_manager.restaurant_manager.oauth2.request;

import lombok.Data;

import java.util.List;

@Data
public class OrderItemRequest {
    private List<MenuItemRequest> menuItems;
    private String note;
}
