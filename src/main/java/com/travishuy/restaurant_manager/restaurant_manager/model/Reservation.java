package com.travishuy.restaurant_manager.restaurant_manager.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

/**
 * Represents a reservation in the restaurant system
 * This class maps to the "reservations" collection in MongoDB database
 *
 * @version 0.1
 * @since 12-02-2025
 * @author TravisHuy
 */
@Document(collection = "reservations")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Reservation {
    /** Unique identifier for the reservation */
    @Id
    private String id;
    /** Scheduled date and time for the reservation*/
    private LocalDateTime reservationTime;
    /** Number of guests expected*/
    private int numberOfPeople;
    /** Customer phone number */
    private String customerPhoneNumber;
    /** Reference to the reserved tables */
    private String tableId;
}
