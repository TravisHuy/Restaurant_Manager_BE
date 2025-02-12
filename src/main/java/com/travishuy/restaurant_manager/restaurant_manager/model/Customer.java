package com.travishuy.restaurant_manager.restaurant_manager.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * Represents a customer entity in the system.
 * This class maps to the "customers" collection in MongoDB database.
 *
 * @author Travis Huy
 * @version 0.1
 * @since 12-02-2025
 */
@Document(collection = "customers")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Customer {
    /** Unique identifier for the customer */
    @Id
    private String id;
    /** Customer's full name */
    private String name;
    /** Customer's phone number for contact */
    private String phoneNumber;
}
