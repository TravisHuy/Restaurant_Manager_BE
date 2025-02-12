package com.travishuy.restaurant_manager.restaurant_manager.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * Represents a category entity in the system.
 * This class maps to the "categories" collection in MongoDB database.
 *
 * @author Travis Huy
 * @version 0.1
 * @since 12-02-2025
 */
@Document(collection = "categories")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Category {
    /** Unique identifier for the category */
    @Id
    private String id;
    /** Name of the category */
    private String name;
}
