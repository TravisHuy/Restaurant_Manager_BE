package com.travishuy.restaurant_manager.restaurant_manager.service;

import com.travishuy.restaurant_manager.restaurant_manager.dto.TableDto;
import com.travishuy.restaurant_manager.restaurant_manager.model.Floor;
import com.travishuy.restaurant_manager.restaurant_manager.model.Order;
import com.travishuy.restaurant_manager.restaurant_manager.model.Reservation;
import com.travishuy.restaurant_manager.restaurant_manager.model.Table;
import com.travishuy.restaurant_manager.restaurant_manager.repository.FloorRepository;
import com.travishuy.restaurant_manager.restaurant_manager.repository.OrderRepository;
import com.travishuy.restaurant_manager.restaurant_manager.repository.ReservationRepository;
import com.travishuy.restaurant_manager.restaurant_manager.repository.TableRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Service class for managing Table entities
 *
 * @author TravisHuy
 * @version 0.1
 * @since 12-02-2025
 */
@Service
public class TableService {
    private final TableRepository tableRepository;
    private final FloorRepository floorRepository;
    private final ReservationRepository reservationRepository;
    private final OrderRepository orderRepository;
    public TableService(TableRepository tableRepository, FloorRepository floorRepository, ReservationRepository reservationRepository,OrderRepository orderRepository) {
        this.tableRepository = tableRepository;
        this.floorRepository = floorRepository;
        this.reservationRepository = reservationRepository;
        this.orderRepository = orderRepository;
    }

    /**
     * Creates a new table in the system
     *
     * @param tableDTO the table data transfer object containing table details
     * @return the created table
     * @throws IllegalArgumentException if table number already exists
     */
    public Table createTable(TableDto tableDTO, String floorId) {
        //check if floor exist
        Floor floor = floorRepository.findById(floorId).orElseThrow(() -> new IllegalArgumentException("Floor not found"));

        List<Table> tablesInFloor = tableRepository.findAllById(floor.getTableIds());

        //check if table exist number already
        boolean tableNumberExists = tablesInFloor.stream()
                .anyMatch(table -> table.getNumber() == tableDTO.getNumber());
        if (tableNumberExists) {
            throw new IllegalArgumentException("Table number already exists");
        }

        // Create a new table entity
        Table table = new Table();
        table.setNumber(tableDTO.getNumber());
        table.setCapacity(tableDTO.getCapacity());
        table.setAvailable(true);
        table.setOrderId("");
        table.setReservationId("");
        table.setFloorId(floorId);

        Table savedTable = tableRepository.save(table);

        floor.getTableIds().add(savedTable.getId());
        floorRepository.save(floor);

        return savedTable;
    }

    /**
     * Retrieves all tables in the system
     *
     * @return a list of all tables
     */
    public List<Table> getAllTables() {
        return tableRepository.findAll();
    }

    /**
     *
     */
    public boolean isTableReserved(String tableId) {
        Optional<Table> tableOptional = tableRepository.findById(tableId);
        if (tableOptional.isPresent()) {
            Table table = tableOptional.get();
            return table.getReservationId() != null && !table.getReservationId().isEmpty();
        }
        throw new IllegalArgumentException("Table not found");
    }

    public List<Table> getTablesFloorById(String floorId) {
        Floor floor = floorRepository.findById(floorId)
                .orElseThrow(() -> new IllegalArgumentException("Floor not found"));

        List<Table> tables = tableRepository.findAllById(floor.getTableIds());

        return tables.stream()
                .sorted(Comparator.comparingInt(Table::getNumber))
                .collect(Collectors.toList());
    }

    public Table getTableByOrderId(String orderId) {
        if(!orderRepository.existsById(orderId)){
            throw new IllegalArgumentException("Order not found");
        }
        return tableRepository.findByOrderId(orderId);
    }
}
