package com.travishuy.restaurant_manager.restaurant_manager.controller;

import com.travishuy.restaurant_manager.restaurant_manager.model.Order;
import com.travishuy.restaurant_manager.restaurant_manager.model.OrderItem;
import com.travishuy.restaurant_manager.restaurant_manager.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controller class for handling order requests
 *
 * @version 0.1
 * @since 24-02-2025
 * @author TravisHuy
 */
@RestController
@RequestMapping("/api/orders")
public class OrderController {

    @Autowired
    private OrderService orderService;

    /**
     * Add a temporary item to an order
     *
     * @param tableId The table ID
     * @param menuItemId The menu item ID
     * @param quantity The quantity of the item
     * @param price The price of the item
     * @return Response with confirmation message
     */
    @PostMapping("/add-item")
    public ResponseEntity<?> addItem(@RequestParam("tableId") String tableId,
                                     @RequestParam("menuItemId") String menuItemId,
                                     @RequestParam("quantity") int quantity,
                                     @RequestParam("price") double price) {
        try {
            orderService.addItemToTempOrder(tableId, menuItemId, quantity, price);
            return ResponseEntity.ok("Item added to temporary order for table: " + tableId);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Failed to add item: " + e.getMessage());
        }
    }

    /**
     * Remove an item from a temporary order
     *
     * @param tableId The table ID
     * @param menuItemId The menu item ID to remove
     * @return Response with confirmation message
     */
    @DeleteMapping("/remove-item")
    public ResponseEntity<?> removeItem(@RequestParam("tableId") String tableId,
                                        @RequestParam("menuItemId") String menuItemId) {
        try {
            orderService.removeItemFromTempOrder(tableId, menuItemId);
            return ResponseEntity.ok("Item removed from temporary order for table: " + tableId);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Failed to remove item: " + e.getMessage());
        }
    }

    /**
     * Confirm and save an order
     *
     * @param tableId The table ID
     * @param customerName The customer name
     * @return The saved order
     */
    @PostMapping("/confirm")
    public ResponseEntity<?> confirmOrder(@RequestParam("tableId") String tableId,
                                          @RequestParam("customerName") String customerName) {
        try {
            Order order = orderService.confirmOrder(tableId, customerName);
            return ResponseEntity.ok(order);
        } catch (IllegalArgumentException | IllegalStateException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Failed to confirm order: " + e.getMessage());
        }
    }

    /**
     * Get all temporary items for a table
     *
     * @param tableId The table ID
     * @return List of temporary order items
     */
    @GetMapping("/temp-items")
    public ResponseEntity<?> getTempItems(@RequestParam("tableId") String tableId) {
        try {
            List<OrderItem> tempItems = orderService.getTempOrderItems(tableId);
            return ResponseEntity.ok(tempItems);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Failed to get temporary items: " + e.getMessage());
        }
    }

    /**
     * Clear all temporary items for a table
     *
     * @param tableId The table ID
     * @return Response with confirmation message
     */
    @DeleteMapping("/clear-temp")
    public ResponseEntity<?> clearTempItems(@RequestParam("tableId") String tableId) {
        try {
            orderService.clearTempItems(tableId);
            return ResponseEntity.ok("Temporary order cleared for table: " + tableId);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Failed to clear temporary order: " + e.getMessage());
        }
    }
}