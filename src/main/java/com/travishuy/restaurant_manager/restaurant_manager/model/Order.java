package com.travishuy.restaurant_manager.restaurant_manager.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
/**
 * Represents an order placed in the system.
 * This class maps to the "orders" collection in MongoDB database.
 *
 * @author Travis Huy
 * @version 0.1
 * @since 12-02-2025
 */
@Document(collection = "orders")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Order {
    /** Unique identifier for the order */
    @Id
    private String id;
    /** Reference to the customer who placed the order */
    private String customerName;
    /** Reference to the table where the order was placed */
    private String tableId;
    /** List of items ordered */
    private List<String> orderItemIds;
    /** Time when the order was placed */
    private LocalDateTime orderTime;
    /** Current status of the order (e.g.,PENDING,IN_PROCESS,COMPLETED) */
    private Status status;
    private double totalAmount;
}
