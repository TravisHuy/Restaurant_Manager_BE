package com.travishuy.restaurant_manager.restaurant_manager.service;

import com.travishuy.restaurant_manager.restaurant_manager.model.Floor;
import com.travishuy.restaurant_manager.restaurant_manager.model.Table;
import com.travishuy.restaurant_manager.restaurant_manager.repository.FloorRepository;
import com.travishuy.restaurant_manager.restaurant_manager.repository.TableRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 * Service class for managing Floor entities
 *
 * @version 0.1
 * @since 14-02-2025
 * @author TravisHuy
 */
@Service
public class FloorService {
    @Autowired
    FloorRepository floorRepository;
    @Autowired
    TableRepository tableRepository;

    /**
     * Creates a new floor in the system
     *
     * @param floor the floor to create
     * @return the created floor
     * @throws IllegalArgumentException if floor name already exists
     */
    public Floor createFloor(Floor floor){
        if(floorRepository.existsByName(floor.getName())){
            throw new IllegalArgumentException("Floor name already exists");
        }
        Floor newFloor = new Floor();
        newFloor.setName(floor.getName());
        newFloor.setTableIds(new ArrayList<>());

        return floorRepository.save(newFloor);
    }
    /**
     * Removes a table from a floor
     *
     *
     * @return floorId the id of the floor
     * @return tableId the id of the table
     * @return the updated floor
     */
    public Floor removeTableFromFloor(String floorId,String tableId){
        Floor floor = floorRepository.findById(floorId).orElseThrow(() -> new IllegalArgumentException("Floor not found"));

        Table table = tableRepository.findById(tableId).orElseThrow(() -> new IllegalArgumentException("Table not found"));

        table.setFloorId(null);

        tableRepository.save(table);

        floor.getTableIds().removeIf(id -> id.equals(tableId));

        return floorRepository.save(floor);
    }
    /**
     * Get all floors
     *
     * @return list of all floors
     */
    public List<Floor> getAllFloors(){
        return floorRepository.findAll();
    }
    /**
     * Get floor by id
     *
     * @param id the id of the floor
     * @return the floor
     * @throws IllegalArgumentException if floor not found
     */
    public Floor getFloorById(String id){
        Floor floor = floorRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Floor not found"));

        List<Table> tables = tableRepository.findAllById(floor.getTableIds());
        tables.sort((t1, t2) -> Integer.compare(t1.getNumber(), t2.getNumber()));

        floor.setTableIds(tables.stream().map(Table::getId).toList());
        return floor;
    }
}
