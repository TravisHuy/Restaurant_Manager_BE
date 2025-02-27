package com.travishuy.restaurant_manager.restaurant_manager.oauth2.request;

import lombok.Data;

@Data
public class MenuItemRequest {
    private String menuItemId;
    private int quantity;
}
