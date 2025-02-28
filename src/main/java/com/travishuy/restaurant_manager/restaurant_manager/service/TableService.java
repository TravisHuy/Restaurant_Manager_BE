package com.travishuy.restaurant_manager.restaurant_manager.service;

import com.travishuy.restaurant_manager.restaurant_manager.dto.TableDto;
import com.travishuy.restaurant_manager.restaurant_manager.model.Floor;
import com.travishuy.restaurant_manager.restaurant_manager.model.Table;
import com.travishuy.restaurant_manager.restaurant_manager.repository.FloorRepository;
import com.travishuy.restaurant_manager.restaurant_manager.repository.TableRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * Service class for managing Table entities
 *
 * @version 0.1
 * @since 12-02-2025
 * @author TravisHuy
 */
@Service
public class TableService {
    private final TableRepository tableRepository;
    private final FloorRepository floorRepository;


    public TableService(TableRepository tableRepository, FloorRepository floorRepository) {
        this.tableRepository = tableRepository;
        this.floorRepository = floorRepository;
    }

    /**
     * Creates a new table in the system
     *
     * @param tableDTO the table data transfer object containing table details
     * @return the created table
     * @throws IllegalArgumentException if table number already exists
     */
    public Table createTable(TableDto tableDTO,String floorId){
        //check if floor exist
        Floor floor = floorRepository.findById(floorId).orElseThrow(() -> new IllegalArgumentException("Floor not found"));

        //check if table exist number already
        boolean tableNumberExistExistsInFloor = floor.getTables().stream().anyMatch(table -> table.getNumber() == tableDTO.getNumber());

        if(tableNumberExistExistsInFloor){
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

        Table saveTable = tableRepository.save(table);

        floor.getTables().add(saveTable);
        floorRepository.save(floor);

        return saveTable;
    }
    /**
     * Retrieves all tables in the system
     *
     * @return a list of all tables
     */
    public List<Table> getAllTables() {
        return tableRepository.findAll();
    }

}
