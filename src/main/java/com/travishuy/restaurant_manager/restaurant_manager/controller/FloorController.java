package com.travishuy.restaurant_manager.restaurant_manager.controller;

import com.travishuy.restaurant_manager.restaurant_manager.model.Floor;
import com.travishuy.restaurant_manager.restaurant_manager.service.FloorService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controller class for handling floor requests
 *
 * @version 0.1
 * @since 14-02-2025
 * @author TravisHuy
 */
@RestController
@RequestMapping("/api/floors")
public class FloorController {

    @Autowired
    FloorService floorService;

    /**
     * Retrieves all floors in the system
     *
     * @return ResponseEntity with a list of all floors
     */
    @GetMapping("/all")
    public ResponseEntity<List<Floor>> getAllFloors(){
        return ResponseEntity.ok(floorService.getAllFloors());
    }
    /**
     * Retrieves a floor by its id
     *
     * @param id the id of the floor
     * @return ResponseEntity with the floor
     */
    @GetMapping("/{id}")
    public ResponseEntity<Floor> getFloorById(@PathVariable String id){
        return ResponseEntity.ok(floorService.getFloorById(id));
    }
    /**
     * Adds a new floor to the system
     *
     * @param floor the floor to add
     * @return ResponseEntity with the created floor
     */
    @PostMapping("/add")
    @PreAuthorize("hasAnyRole('ADMIN','MANAGER')")
    public ResponseEntity<Floor> createFloor(@Valid @RequestBody Floor floor){
        return ResponseEntity.ok(floorService.createFloor(floor));
    }

    /**
     * Removes a table from a floor
     *
     * @param floorId the id of the floor
     * @param tableId the id of the table
     * @return ResponseEntity with the updated floor
     */
    public ResponseEntity<Floor> removeTableFromFloor(@PathVariable String floorId, @PathVariable String tableId){
        return ResponseEntity.ok(floorService.removeTableFromFloor(floorId,tableId));
    }
}
