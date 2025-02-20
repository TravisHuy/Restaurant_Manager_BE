package com.travishuy.restaurant_manager.restaurant_manager.controller;

import com.travishuy.restaurant_manager.restaurant_manager.dto.MenuItemDTO;
import com.travishuy.restaurant_manager.restaurant_manager.model.MenuItem;
import com.travishuy.restaurant_manager.restaurant_manager.service.MenuItemService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
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
     * Adds a new menu item to the system
     *
     * @param menuItemDTO the menu item to add
     * @param categoryId the id of the category
     * @return ResponseEntity with the created menu item
     */
    @PostMapping(value = "/add/{categoryId}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> creatMenuItem(@ModelAttribute MenuItemDTO menuItemDTO,@PathVariable String categoryId) {
        try {
            MenuItem createItem = menuItemService.createMenuItem(menuItemDTO,categoryId);
            return new ResponseEntity<>(createItem, HttpStatus.CREATED);
        }
        catch (IllegalArgumentException e){
            Map<String, String> response = new HashMap<>();
            response.put("message", e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }
        catch (IOException e) {
            Map<String, String> response = new HashMap<>();
            response.put("message", "Failed to process image: " + e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Retrieves an image of a menu item by its id
     *
     * @param id the id of the menu item
     * @return ResponseEntity with the image
     */
    @GetMapping("/{id}/image")
    public ResponseEntity<byte[]> getMenuItemImage(@PathVariable String id) throws IOException {
        byte[] imageBytes= menuItemService.getImage(id);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.IMAGE_JPEG);
        return new ResponseEntity<>(imageBytes,headers,HttpStatus.OK);
    }

    @GetMapping("/all")
    public ResponseEntity<List<MenuItem>> getAllMenuItems() {
        return ResponseEntity.ok(menuItemService.getAllMenuItems());
    }

    @GetMapping("/all/{categoryId}")
    public ResponseEntity<?> getAllMenuItemsByCategory(@PathVariable String categoryId) {
        try {
            List<MenuItem> menuItems = menuItemService.getMenuItemsByCategory(categoryId);
            return new ResponseEntity<>(menuItems, HttpStatus.OK);
        } catch (IllegalArgumentException e) {
            Map<String, String> response = new HashMap<>();
            response.put("message", e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }
    }
}
