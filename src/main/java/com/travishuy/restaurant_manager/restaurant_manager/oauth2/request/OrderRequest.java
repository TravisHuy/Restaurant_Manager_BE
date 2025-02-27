package com.travishuy.restaurant_manager.restaurant_manager.oauth2.request;

import lombok.Data;

import java.util.List;
@Data
public class OrderRequest {
    private String customerName;
    private String tableId;
    private List<OrderItemRequest> items;
}
