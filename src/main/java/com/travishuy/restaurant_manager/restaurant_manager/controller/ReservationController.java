package com.travishuy.restaurant_manager.restaurant_manager.controller;

import com.travishuy.restaurant_manager.restaurant_manager.dto.ReservationDTO;
import com.travishuy.restaurant_manager.restaurant_manager.model.Reservation;
import com.travishuy.restaurant_manager.restaurant_manager.service.ReservationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

/**
 * Controller class for handling reservation requests
 *
 * @version 0.1
 * @since 28-02-2025
 * @author TravisHuy
 */
@RestController
@RequestMapping("/api/reservations")
public class ReservationController {

    @Autowired
    ReservationService reservationService;

    @PostMapping("/add/{tableId}")
    public ResponseEntity<Reservation> addReservation(
            @PathVariable String tableId, @RequestBody ReservationDTO reservationDTO
            ){
        try{
            return ResponseEntity.ok(reservationService.addReservation(tableId,reservationDTO));
        }
        catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        }
    }
}
