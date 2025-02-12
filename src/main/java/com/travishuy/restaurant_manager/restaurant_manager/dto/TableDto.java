package com.travishuy.restaurant_manager.restaurant_manager.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Data Transfer Object for Table creation
 *
 * @version 0.1
 * @since 12-02-2025
 * @author TravisHuy
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class TableDto {
    /** Table number */
    private int number;
    /** Table capacity */
    private int capacity;
    /** Availability status of the table is true*/
    private boolean available = true;
}
