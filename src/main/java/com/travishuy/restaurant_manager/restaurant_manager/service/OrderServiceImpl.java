package com.travishuy.restaurant_manager.restaurant_manager.service;

import com.travishuy.restaurant_manager.restaurant_manager.dto.MenuDTO;
import com.travishuy.restaurant_manager.restaurant_manager.dto.OrderItemDTO;
import com.travishuy.restaurant_manager.restaurant_manager.model.MenuItem;
import com.travishuy.restaurant_manager.restaurant_manager.model.Order;
import com.travishuy.restaurant_manager.restaurant_manager.model.OrderItem;
import com.travishuy.restaurant_manager.restaurant_manager.model.Status;
import com.travishuy.restaurant_manager.restaurant_manager.oauth2.request.MenuItemRequest;
import com.travishuy.restaurant_manager.restaurant_manager.oauth2.request.OrderItemRequest;
import com.travishuy.restaurant_manager.restaurant_manager.oauth2.request.OrderRequest;
import com.travishuy.restaurant_manager.restaurant_manager.oauth2.response.OrderResponse;
import com.travishuy.restaurant_manager.restaurant_manager.repository.MenuItemRepository;
import com.travishuy.restaurant_manager.restaurant_manager.repository.OrderItemRepository;
import com.travishuy.restaurant_manager.restaurant_manager.repository.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
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


    @Override
    public OrderResponse createOrder(OrderRequest orderRequest) {
        List<String> orderItemIds = new ArrayList<>();
        double totalOrderAmount =0.0;

        for(OrderItemRequest itemRequest: orderRequest.getItems()){
            OrderItem orderItem = createOrderItemFromRequest(itemRequest);
            OrderItem saveOrderItem = orderItemRepository.save(orderItem);

            orderItemIds.add(saveOrderItem.getId());
            totalOrderAmount += saveOrderItem.getTotalPrice();
        }

        //create the order

        Order order = new Order();
        order.setCustomerName(orderRequest.getCustomerName());
        order.setTableId(orderRequest.getTableId());
        order.setOrderItemIds(orderItemIds);
        order.setOrderTime(LocalDateTime.now());
        order.setStatus(Status.IN_PROCESS);
        order.setTotalAmount(totalOrderAmount);

        Order savedOrder = orderRepository.save(order);

        return mapToOrderResponse(savedOrder);
    }

    private OrderItem createOrderItemFromRequest(OrderItemRequest itemRequest){
        List<OrderItem.OrderItemDetail> orderItemDetails = new ArrayList<>();
        double totalPrice = 0.0;

        // Process each MenuItemRequest
        for (MenuItemRequest menuItemRequest : itemRequest.getMenuItems()) {
            // Fetch the MenuItem to get its details
            MenuItem menuItem = menuItemRepository.findById(menuItemRequest.getMenuItemId())
                    .orElseThrow(() -> new RuntimeException("MenuItem not found with ID: " + menuItemRequest.getMenuItemId()));

            // Calculate item price
            double itemPrice = menuItem.getPrice() * menuItemRequest.getQuantity();
            totalPrice += itemPrice;

            // Create OrderItemDetail
            OrderItem.OrderItemDetail detail = new OrderItem.OrderItemDetail(
                    menuItem.getId(),
                    menuItem.getName(),
                    menuItemRequest.getQuantity(),
                    itemPrice
            );

            orderItemDetails.add(detail);
        }

        // Create and return OrderItem
        OrderItem orderItem = new OrderItem();
        orderItem.setMenuItemIds(orderItemDetails);
        orderItem.setTotalPrice(totalPrice);
        orderItem.setNote(itemRequest.getNote());

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
}
