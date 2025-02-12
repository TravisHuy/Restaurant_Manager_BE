package com.travishuy.restaurant_manager.restaurant_manager.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

/**
 * Represents an inventory item in the restaurant system
 * This class maps to the "inventory" collection in MongoDB database.
 *
 * @version 0.1
 * @since 12-02-2025
 * @author TravisHuy
 */
@Document(collection = "inventory")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Inventory {
    /** Unique identifier for the inventory item */
    @Id
    private String id;
    /** Name of the inventory item */
    private String itemName;
    /** Quantity of the inventory item */
    private int quantity;
    /** Last updated date and time */
    private LocalDateTime lastUpdated;
}
