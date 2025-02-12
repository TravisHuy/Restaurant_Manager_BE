package com.travishuy.restaurant_manager.restaurant_manager.service;

import com.travishuy.restaurant_manager.restaurant_manager.dto.TableDto;
import com.travishuy.restaurant_manager.restaurant_manager.model.Table;
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

    public TableService(TableRepository tableRepository) {
        this.tableRepository = tableRepository;
    }

    /**
     * Creates a new table in the system
     *
     * @param tableDTO the table data transfer object containing table details
     * @return the created table
     * @throws IllegalArgumentException if table number already exists
     */
    public Table createTable(TableDto tableDTO){

        //check if table exist number already
        if(tableRepository.existsByNumber(tableDTO.getNumber())){
            throw new IllegalArgumentException("Table number already exists");
        }

        // Create a new table entity
        Table table = new Table();
        table.setNumber(tableDTO.getNumber());
        table.setCapacity(tableDTO.getCapacity());
        table.setAvailable(tableDTO.isAvailable());
        table.setOrderIds(new ArrayList<>());
        table.setReservationIds(new ArrayList<>());

        return tableRepository.save(table);
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
