package com.travishuy.restaurant_manager.restaurant_manager.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
/**
 * Represents a menu item available in the restaurant.
 * This class maps to the "menu_items" collection in MongoDB database.
 *
 * @author Travis Huy
 * @version 0.1
 * @since 12-02-2025
 */
@Document(collection = "menu_items")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class MenuItem {
    /** Unique identifier for the menu item */
    @Id
    private String id;
    /** Name of the dish */
    private String name;
    /** Detailed description of the dish */
    private String description;
    /** Price of the dish */
    private double price;
    /** Category of the dish (e.g., appetizer, main course, dessert) */
    private String categoryId;
    /** Indicates if the item is currently available */
    private boolean available;
}
