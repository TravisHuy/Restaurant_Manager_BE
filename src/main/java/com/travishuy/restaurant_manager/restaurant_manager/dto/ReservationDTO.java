package com.travishuy.restaurant_manager.restaurant_manager.dto;

import lombok.Data;

@Data
public class ReservationDTO {
    /** Number of guests expected*/
    private int numberOfPeople;
    /** Customer phone number */
    private String customerName;
}
