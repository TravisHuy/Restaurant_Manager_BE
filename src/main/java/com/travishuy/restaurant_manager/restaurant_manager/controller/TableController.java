package com.travishuy.restaurant_manager.restaurant_manager.controller;

import com.travishuy.restaurant_manager.restaurant_manager.dto.TableDto;
import com.travishuy.restaurant_manager.restaurant_manager.model.Table;
import com.travishuy.restaurant_manager.restaurant_manager.service.TableService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controller class for handling table requests
 *
 * @version 0.1
 * @since 12-02-2025
 * @author TravisHuy
 */
@RestController
@RequestMapping("/api/tables")
public class TableController {

    @Autowired
    TableService tableService;

    @GetMapping("/alls")
    public ResponseEntity<List<Table>> getAllTables() {
        return ResponseEntity.ok(tableService.getAllTables());
    }
    /**
     * Adds a new table to the system
     *
     * @param tableDto the table data transfer object containing table details
     * @return ResponseEntity with the created table
     */
    @PostMapping("/add/{floorId}")
    @PreAuthorize("hasAnyRole('ADMIN','MANAGER')")
    public ResponseEntity<?> addTable(@Valid @RequestBody TableDto tableDto, @PathVariable String floorId) {
        try {
            return ResponseEntity.ok(tableService.createTable(tableDto,floorId));
        }
        catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/check/{tableId}")
    public ResponseEntity<?> checkTableReservation(@PathVariable String tableId){
        try {
            boolean isReserved = tableService.isTableReserved(tableId);
            return ResponseEntity.ok(isReserved);
        }
        catch (IllegalArgumentException e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/byFloor/{floorId}")
    public ResponseEntity<?> getTableByFloorId(@PathVariable String floorId){
        try {
            List<Table> tables = tableService.getTablesFloorById(floorId);
            return ResponseEntity.ok(tables);
        }
        catch (IllegalArgumentException e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
