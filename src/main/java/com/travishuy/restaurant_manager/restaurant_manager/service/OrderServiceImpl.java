package com.travishuy.restaurant_manager.restaurant_manager.service;

import com.travishuy.restaurant_manager.restaurant_manager.dto.MenuDTO;
import com.travishuy.restaurant_manager.restaurant_manager.dto.OrderItemDTO;
import com.travishuy.restaurant_manager.restaurant_manager.model.*;
import com.travishuy.restaurant_manager.restaurant_manager.oauth2.request.MenuItemRequest;
import com.travishuy.restaurant_manager.restaurant_manager.oauth2.request.OrderItemRequest;
import com.travishuy.restaurant_manager.restaurant_manager.oauth2.request.OrderRequest;
import com.travishuy.restaurant_manager.restaurant_manager.oauth2.response.OrderResponse;
import com.travishuy.restaurant_manager.restaurant_manager.repository.MenuItemRepository;
import com.travishuy.restaurant_manager.restaurant_manager.repository.OrderItemRepository;
import com.travishuy.restaurant_manager.restaurant_manager.repository.OrderRepository;
import com.travishuy.restaurant_manager.restaurant_manager.repository.TableRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 *
 */
@Service
public class OrderServiceImpl implements OrderService{

    @Autowired
    OrderRepository orderRepository;
    @Autowired
    OrderItemRepository orderItemRepository;
    @Autowired
    MenuItemRepository menuItemRepository;
    @Autowired
    TableRepository tableRepository;

    @Override
    public OrderResponse createOrder(OrderRequest orderRequest) {

        // Create a single OrderItem containing all menu items
        OrderItem orderItem = createOrderItemFromRequest(orderRequest.getItems());
        OrderItem savedOrderItem = orderItemRepository.save(orderItem);

        // Create the order with single OrderItem
        Order order = new Order();
        order.setCustomerName(orderRequest.getCustomerName());
        order.setTableId(orderRequest.getTableId());
        order.setOrderItemIds(Collections.singletonList(savedOrderItem.getId()));
        order.setOrderTime(LocalDateTime.now());
        order.setStatus(Status.IN_PROCESS);
        order.setTotalAmount(savedOrderItem.getTotalPrice());

        Order savedOrder = orderRepository.save(order);

        Table table = tableRepository.findById(orderRequest.getTableId()).orElseThrow(
                () -> new RuntimeException("Table not found with ID: "+ orderRequest.getTableId())
        );
        table.setAvailable(false);

        List<String> currentOrderIds = table.getOrderIds();
        if(currentOrderIds == null) {
            currentOrderIds = new ArrayList<>();
        }
        currentOrderIds.add(savedOrder.getId());
        table.setOrderIds(currentOrderIds);
        tableRepository.save(table);

        return mapToOrderResponse(savedOrder);
    }

    private OrderItem createOrderItemFromRequest(List<OrderItemRequest> itemRequests) {
        List<OrderItem.OrderItemDetail> orderItemDetails = new ArrayList<>();
        double totalPrice = 0.0;

        // Process all menu items from all requests into a single OrderItem
        for (OrderItemRequest itemRequest : itemRequests) {
            for (MenuItemRequest menuItemRequest : itemRequest.getMenuItems()) {
                MenuItem menuItem = menuItemRepository.findById(menuItemRequest.getMenuItemId())
                        .orElseThrow(() -> new RuntimeException("MenuItem not found with ID: " + menuItemRequest.getMenuItemId()));

                double itemPrice = menuItem.getPrice() * menuItemRequest.getQuantity();
                totalPrice += itemPrice;

                OrderItem.OrderItemDetail detail = new OrderItem.OrderItemDetail(
                        menuItem.getId(),
                        menuItem.getName(),
                        menuItemRequest.getQuantity(),
                        menuItem.getPrice()
                );

                orderItemDetails.add(detail);
            }

        }

        OrderItem orderItem = new OrderItem();
        orderItem.setMenuItemIds(orderItemDetails);
        orderItem.setTotalPrice(totalPrice);

        // Set note if provided in the first request (optional)
        if (!itemRequests.isEmpty() && itemRequests.get(0).getNote() != null) {
            orderItem.setNote(itemRequests.get(0).getNote());
        }


        return orderItem;
    }

    private OrderResponse mapToOrderResponse(Order order) {
        OrderResponse response = new OrderResponse();
        response.setId(order.getId());
        response.setCustomerName(order.getCustomerName());
        response.setTableId(order.getTableId());
        response.setOrderTime(order.getOrderTime());
        response.setStatus(order.getStatus());
        response.setTotalAmount(order.getTotalAmount());

        // Fetch and map OrderItems
        List<OrderItemDTO> orderItemDTOs = order.getOrderItemIds().stream()
                .map(this::getOrderItemDTO)
                .collect(Collectors.toList());

        response.setItems(orderItemDTOs);
        return response;
    }

    private OrderItemDTO getOrderItemDTO(String orderItemId) {
        OrderItem orderItem = orderItemRepository.findById(orderItemId)
                .orElseThrow(() -> new RuntimeException("OrderItem not found with ID: " + orderItemId));

        List<MenuDTO> menuDTOs = orderItem.getMenuItemIds().stream()
                .map(detail -> new MenuDTO(
                        detail.getMenuItemId(),
                        detail.getMenuItemName(),
                        detail.getQuantity(),
                        detail.getPrice()))
                .collect(Collectors.toList());

        return new OrderItemDTO(
                orderItem.getId(),
                menuDTOs,
                orderItem.getTotalPrice(),
                orderItem.getNote()
        );
    }

    @Override
    public Optional<String> getCustomerName(String tableId){
        return orderRepository.findByTableId(tableId)
                .map(Order::getCustomerName);
    }
}
