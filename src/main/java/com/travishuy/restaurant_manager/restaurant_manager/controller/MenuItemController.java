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
/**
 * Controller class for handling menuItem requests
 *
 * @version 0.1
 * @since 14-02-2025
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
    @PostMapping(value = "/add", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<MenuItem> creatMenuItem(@ModelAttribute MenuItemDTO menuItemDTO,@PathVariable String categoryId) throws IOException {
        MenuItem createItem = menuItemService.createMenuItem(menuItemDTO,categoryId);
        return new ResponseEntity<>(createItem, HttpStatus.CREATED);
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
}
