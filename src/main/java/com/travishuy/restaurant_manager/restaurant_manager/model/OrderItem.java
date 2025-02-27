package com.travishuy.restaurant_manager.restaurant_manager.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

/**
 * Represents an order_items placed in the system.
 * This class maps to the "order_items" collection in MongoDB database.
 *
 * @version 0.1
 * @since 12-02-2025
 * @author Travis Huy
 */
@Document(collection = "order_items")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderItem {
    /** Unique identifier for the order item*/
    @Id
    private String id;
    private List<OrderItemDetail> menuItemIds;
    private double totalPrice;
    private String note;


    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class OrderItemDetail {
        private String menuItemId;
        private String menuItemName;
        private int quantity;
        private double price;
    }

}
