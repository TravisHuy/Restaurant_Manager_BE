package com.travishuy.restaurant_manager.restaurant_manager.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;
/**
 * Represents a table in the restaurant system
 * This class maps to the "tables" collection in MongoDB database
 *
 * @version 0.1
 * @since 12-02-2025
 * @author TravisHuy
 */
@Document(collection = "tables")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Table {
    /** Unique identifier for the table */
    @Id
    private String id;
    /** Table number */
    private int number;
    /** Table capacity */
    private int capacity;
    /** Availability status of the table */
    private boolean available;
    /** List of order ids associated with the table */
    private List<String> orderIds;
    /** List of reservation ids associated with the table */
    private List<String> reservationIds;
    /** Floor id of the table */
    private String floorId;
}
