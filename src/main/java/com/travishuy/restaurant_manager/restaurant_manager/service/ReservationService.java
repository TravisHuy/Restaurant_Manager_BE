package com.travishuy.restaurant_manager.restaurant_manager.service;

import com.travishuy.restaurant_manager.restaurant_manager.dto.ReservationDTO;
import com.travishuy.restaurant_manager.restaurant_manager.model.NotificationType;
import com.travishuy.restaurant_manager.restaurant_manager.model.Reservation;
import com.travishuy.restaurant_manager.restaurant_manager.model.Table;
import com.travishuy.restaurant_manager.restaurant_manager.repository.ReservationRepository;
import com.travishuy.restaurant_manager.restaurant_manager.repository.TableRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

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

    @Autowired
    NotificationService notificationService;

    public Reservation addReservation(String tableId, ReservationDTO reservationDTO){
        Table table =  tableRepository.findById(tableId).orElseThrow(()-> new IllegalArgumentException("Not found table Id"));

        if(!table.isAvailable()){
            throw  new IllegalArgumentException("The table is reserved");
        }

        if(table.getCapacity()  <  reservationDTO.getNumberOfPeople()) {
            throw new IllegalArgumentException("Table capacity is not sufficient for " +
                    reservationDTO.getNumberOfPeople() + " people");
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

        notificationService.createNotification(
                "New Reservation",
                "Table " + table.getNumber() + " reserved for " + reservationDTO.getNumberOfPeople() +
                        " people by " + reservationDTO.getCustomerName(),
                NotificationType.RESERVATION,
                reservation.getId()
        );

        return reservation;
    }


    /**
     * check if a table is already reserved
     *
     * @param tableId ID of the table to check
     * @return true if the table is reserved, false otherwise
     */
    public boolean isTableReserved(String tableId){
        Optional<Table> tableOptional = tableRepository.findById(tableId);
        if(tableOptional.isPresent()){
            return !tableOptional.get().isAvailable();
        }
        throw new IllegalArgumentException("Table not found with ID: "+ tableId);
    }

    /**
     * Gets a reservation by its ID
     *
     * @param reservationId ID of the reservation
     * @return the reservation if exists
     */
    public Reservation getReservation(String reservationId) {
        try {
            if (reservationId == null || reservationId.trim().isEmpty()) {
                throw new IllegalArgumentException("Reservation cannot be null or empty");
            }

            return reservationRepository.findById(reservationId)
                    .orElseThrow(() -> new RuntimeException("Reservation not found with ID: " + reservationId));
        } catch (IllegalArgumentException e) {
            throw e;
        } catch (Exception e) {
            throw new RuntimeException("Failed to retrieve Id: " + e.getMessage(), e);
        }
    }


    /**
     * gets the active reservation for a table
     *
     * @param tableId ID of the table
     * @return the reservation if exists
     */
    public Reservation getTableReservation(String tableId){
        Table table = tableRepository.findById(tableId)
                .orElseThrow(() -> new IllegalArgumentException("table not found"));

        if(table.getReservationId() !=null && !table.getReservationId().isEmpty()){
            return reservationRepository.findById(table.getReservationId())
                    .orElse(null);
        }
        return null;
    }

    /**
     * Cancels a reservation and makes the table available again
     *
     * @param reservationId ID of the reservation to cancel
     * @return the canceled reservation
     */
    public Reservation cancelReservation(String reservationId){
        Reservation reservation = reservationRepository.findById(reservationId)
                .orElseThrow(() -> new IllegalArgumentException("Reservation not found"));

        // Get the table and make it available again
        Table table = tableRepository.findById(reservation.getTableId())
                .orElseThrow(() -> new IllegalArgumentException("Table not found"));

        table.setAvailable(true);
        table.setReservationId("");
        tableRepository.save(table);

        reservationRepository.delete(reservation);

        return reservation;
    }

}
