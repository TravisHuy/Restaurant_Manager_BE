package com.travishuy.restaurant_manager.restaurant_manager.service;

import com.travishuy.restaurant_manager.restaurant_manager.dto.ReservationDTO;
import com.travishuy.restaurant_manager.restaurant_manager.model.Reservation;
import com.travishuy.restaurant_manager.restaurant_manager.model.Table;
import com.travishuy.restaurant_manager.restaurant_manager.repository.ReservationRepository;
import com.travishuy.restaurant_manager.restaurant_manager.repository.TableRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

/**
 * Service class for managing Reservation entities
 *
 * @version 0.1
 * @since 28-02-2025
 * @author TravisHuy
 */
@Service
public class ReservationService {

    @Autowired
    ReservationRepository reservationRepository;

    @Autowired
    TableRepository tableRepository;

    public Reservation addReservation(String tableId, ReservationDTO reservationDTO){
        Table table =  tableRepository.findById(tableId).orElseThrow(()-> new IllegalArgumentException("Not found table Id"));

        if(!table.isAvailable()){
            throw  new IllegalArgumentException("The table is reserved");
        }


        Reservation reservation = new Reservation();

        reservation.setTableId(tableId);
        reservation.setReservationTime(LocalDateTime.now());
        reservation.setNumberOfPeople(reservationDTO.getNumberOfPeople());
        reservation.setCustomerName(reservationDTO.getCustomerName());

        reservation = reservationRepository.save(reservation);

        table.setAvailable(false);
        table.setReservationId(reservation.getId());
        tableRepository.save(table);


        return reservation;
    }
}
