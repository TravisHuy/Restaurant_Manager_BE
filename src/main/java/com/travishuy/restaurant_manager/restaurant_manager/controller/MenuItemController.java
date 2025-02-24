package com.travishuy.restaurant_manager.restaurant_manager.controller;

import com.travishuy.restaurant_manager.restaurant_manager.dto.MenuItemDTO;
import com.travishuy.restaurant_manager.restaurant_manager.service.MenuItemService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.data.mongodb.gridfs.GridFsResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;

/**
 * Controller class for handling menuItem requests
 *
 * @version 0.1
 * @since 18-02-2025
 * @author TravisHuy
 */
@RestController
@RequestMapping("api/menu-items")
@RequiredArgsConstructor
public class MenuItemController {
    @Autowired
    MenuItemService menuItemService;

    /**
     * Create a new menu item with an optional image
     *
     * @param menuItemDTO The menu item data
     * @param imageFile Optional image file for the menu item
     * @return The created menu item
     *
     */
    @PostMapping(value = "/add/{categoryId}",consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    public ResponseEntity<MenuItemDTO> createMenuItem(
            @RequestPart("menuItem") MenuItemDTO menuItemDTO,
            @RequestPart(value = "image", required = false) MultipartFile imageFile,
            @PathVariable String categoryId
    ) {
        try {
            MenuItemDTO createdItem = menuItemService.createMenuItem(menuItemDTO, imageFile,categoryId);
            return ResponseEntity.status(HttpStatus.CREATED).body(createdItem);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * Update an existing menu item
     *
     * @param id The ID of the menu item to update
     * @param menuItemDTO Updated menu item data
     * @param imageFile Optional new image file
     * @return The updated menu item
     */
    @PutMapping(value = "/{id}", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    public ResponseEntity<MenuItemDTO> updateMenuItem(
            @PathVariable String id,
            @RequestPart("menuItem") MenuItemDTO menuItemDTO,
            @RequestPart(value = "image", required = false) MultipartFile imageFile
    ) {
        try {
            MenuItemDTO updatedItem = menuItemService.updateMenuItem(id, menuItemDTO, imageFile);
            return ResponseEntity.ok(updatedItem);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * Retrieves all menu items in the system
     *
     * @return ResponseEntity with a list of all menu items
     */
    @GetMapping("/all")
    public ResponseEntity<?> getAllMenuItems() {
        return ResponseEntity.ok(menuItemService.getAllMenuItems());
    }


    /**
     * Updates a menu item by adding a note.
     *
     * @param id   The ID of the menu item.
     * @param note The note to be added.
     * @return {@code ResponseEntity<MenuItemDTO>} if successful, otherwise returns an error response.
     */
    @PutMapping("/addNote/{id}")
    public ResponseEntity<?> addNoteMenuItem(@PathVariable String id, @RequestParam String note) {
        try {
            MenuItemDTO menuItemDTO = menuItemService.addNoteMenuItem(id,note);
            return ResponseEntity.ok(menuItemDTO);
        }
        catch (IllegalArgumentException e){
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
        catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "An unexpected error occurred"));
        }
    }
}
