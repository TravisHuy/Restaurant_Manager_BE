package com.travishuy.restaurant_manager.restaurant_manager.service;

import com.travishuy.restaurant_manager.restaurant_manager.model.Order;
import com.travishuy.restaurant_manager.restaurant_manager.model.OrderItem;
import com.travishuy.restaurant_manager.restaurant_manager.model.Status;
import com.travishuy.restaurant_manager.restaurant_manager.repository.OrderItemRepository;
import com.travishuy.restaurant_manager.restaurant_manager.repository.OrderRepository;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class OrderService {

    @Autowired
    private OrderItemRepository orderItemRepository;

    @Autowired
    private OrderRepository orderRepository;

    // Constants for session keys
    private static final String TEMP_ORDER_ITEMS_PREFIX = "tempOrderItems";

    /**
     * Get current HTTP session
     * @return The current HTTP session
     */
    public HttpSession getSession() {
        ServletRequestAttributes attr = (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();
        return attr.getRequest().getSession(true); // Create session if it doesn't exist
    }

    /**
     * Get the session key for a specific table
     * @param tableId The table ID
     * @return The session key for the table
     */
    private String getTableSessionKey(String tableId) {
        return TEMP_ORDER_ITEMS_PREFIX + "_" + tableId;
    }

    /**
     * Add a menu item to the temporary order for a table
     * @param tableId The table ID
     * @param menuItemId The menu item ID
     * @param quantity The quantity of the item
     * @param price The price of the item
     */
    public void addItemToTempOrder(String tableId, String menuItemId, int quantity, double price) {
        if (tableId == null || menuItemId == null || quantity <= 0) {
            throw new IllegalArgumentException("Invalid order parameters");
        }

        HttpSession session = getSession();
        String sessionKey = getTableSessionKey(tableId);

        @SuppressWarnings("unchecked")
        List<OrderItem> tempOrderItems = (List<OrderItem>) session.getAttribute(sessionKey);

        if (tempOrderItems == null) {
            tempOrderItems = new ArrayList<>();
        }

        // Check if the item already exists to update quantity instead of adding duplicate
        Optional<OrderItem> existingItem = tempOrderItems.stream()
                .filter(item -> item.getMenuItemIds().contains(menuItemId))
                .findFirst();

        if (existingItem.isPresent()) {
            // Update existing item quantity
            OrderItem item = existingItem.get();
            item.setQuantity(item.getQuantity() + quantity);
            item.setPrice(item.getPrice() + price);
        } else {
            // Add new item
            OrderItem newItem = new OrderItem(null, List.of(menuItemId), quantity, price);
            tempOrderItems.add(newItem);
        }

        session.setAttribute(sessionKey, tempOrderItems);
    }

    /**
     * Remove a menu item from the temporary order for a table
     * @param tableId The table ID
     * @param menuItemId The menu item ID to remove
     */
    public void removeItemFromTempOrder(String tableId, String menuItemId) {
        if (tableId == null || menuItemId == null) {
            throw new IllegalArgumentException("Table ID and Menu Item ID cannot be null");
        }

        HttpSession session = getSession();
        String sessionKey = getTableSessionKey(tableId);

        @SuppressWarnings("unchecked")
        List<OrderItem> tempOrderItems = (List<OrderItem>) session.getAttribute(sessionKey);

        if (tempOrderItems != null) {
            tempOrderItems.removeIf(item -> item.getMenuItemIds().contains(menuItemId));

            if (tempOrderItems.isEmpty()) {
                session.removeAttribute(sessionKey);
            } else {
                session.setAttribute(sessionKey, tempOrderItems);
            }
        }
    }

    /**
     * Confirm an order and save it to the database
     * @param tableId The table ID
     * @param customerName The customer name
     * @return The saved order
     */
    @Transactional
    public Order confirmOrder(String tableId, String customerName) {
        if (tableId == null || customerName == null || customerName.trim().isEmpty()) {
            throw new IllegalArgumentException("Table ID and customer name are required");
        }

        HttpSession session = getSession();
        String sessionKey = getTableSessionKey(tableId);

        @SuppressWarnings("unchecked")
        List<OrderItem> tempOrderItems = (List<OrderItem>) session.getAttribute(sessionKey);

        if (tempOrderItems == null || tempOrderItems.isEmpty()) {
            throw new IllegalStateException("No items to confirm for table " + tableId);
        }

        List<String> orderItemIds = new ArrayList<>();

        try {
            // Save all order items and collect their IDs
            for (OrderItem item : tempOrderItems) {
                OrderItem savedItem = orderItemRepository.save(item);
                orderItemIds.add(savedItem.getId());
            }

            // Create and save the order
            Order order = new Order(
                    null,
                    customerName,
                    tableId,
                    null,
                    orderItemIds,
                    LocalDateTime.now(),
                    Status.PENDING
            );

            Order savedOrder = orderRepository.save(order);

            // Clear the temporary items from session after successful order
            session.removeAttribute(sessionKey);

            return savedOrder;
        } catch (Exception e) {
            // Log the exception here
            throw new RuntimeException("Failed to confirm order: " + e.getMessage(), e);
        }
    }

    /**
     * Get temporary order items for a table
     * @param tableId The table ID
     * @return List of temporary order items
     */
    public List<OrderItem> getTempOrderItems(String tableId) {
        if (tableId == null) {
            throw new IllegalArgumentException("Table ID cannot be null");
        }

        HttpSession session = getSession();
        String sessionKey = getTableSessionKey(tableId);

        @SuppressWarnings("unchecked")
        List<OrderItem> tempOrderItems = (List<OrderItem>) session.getAttribute(sessionKey);

        return tempOrderItems != null ? new ArrayList<>(tempOrderItems) : new ArrayList<>();
    }

    /**
     * Clear all temporary items for a table
     * @param tableId The table ID
     */
    public void clearTempItems(String tableId) {
        if (tableId == null) {
            throw new IllegalArgumentException("Table ID cannot be null");
        }

        HttpSession session = getSession();
        String sessionKey = getTableSessionKey(tableId);
        session.removeAttribute(sessionKey);
    }
}