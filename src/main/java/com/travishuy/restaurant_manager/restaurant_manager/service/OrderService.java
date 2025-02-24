package com.travishuy.restaurant_manager.restaurant_manager.service;

import com.travishuy.restaurant_manager.restaurant_manager.model.Order;
import com.travishuy.restaurant_manager.restaurant_manager.model.OrderItem;
import com.travishuy.restaurant_manager.restaurant_manager.repository.OrderItemRepository;
import com.travishuy.restaurant_manager.restaurant_manager.repository.OrderRepository;
import jakarta.servlet.ServletRequestAttributeEvent;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class OrderService {
    @Autowired
    OrderItemRepository orderItemRepository;
    @Autowired
    OrderRepository orderRepository;

    private static final String TEMP_ORDER_ITEMS_KEY = "tempOrderItems";

    public HttpSession getSession() {
        ServletRequestAttributes attr = (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();
        return attr.getRequest().getSession(true);
    }

    public void addItemToTempOrder(String tableId, String menuItemId, int quantity, double price) {
        HttpSession session = getSession();
        List<OrderItem> tempOrderItems = (List<OrderItem>) session.getAttribute(TEMP_ORDER_ITEMS_KEY +"_"+tableId);
        if(tempOrderItems == null){
            tempOrderItems = new ArrayList<>();
        }
        OrderItem newItem = new OrderItem(null, List.of(menuItemId), quantity, price);
        tempOrderItems.add(newItem);
        session.setAttribute( TEMP_ORDER_ITEMS_KEY + "_" + tableId, tempOrderItems);
    }

    //Cancel
    public void remoteItemFromTempOrder(String tableId, String menuItemId) {
        HttpSession session = getSession();
        String key = TEMP_ORDER_ITEMS_KEY  + "_" + tableId;
        List<OrderItem> tempOrderItems = (List<OrderItem>) session.getAttribute(key);
        if (tempOrderItems != null) {
            tempOrderItems.removeIf(item -> item.getMenuItemIds().contains(menuItemId));
            if (tempOrderItems.isEmpty()) {
                session.removeAttribute(key);
            } else {
                session.setAttribute(key, tempOrderItems);
            }
        }

    }

    public Order confirmOrder(String tableId,String customerName){
        HttpSession session = getSession();
        String key = TEMP_ORDER_ITEMS_KEY + "_" + tableId;
        List<OrderItem> tempOrderItems = (List<OrderItem>) session.getAttribute(key);
        if (tempOrderItems == null || tempOrderItems.isEmpty()) {
            throw new IllegalStateException("No items to confirm for table " + tableId);
        }

        List<String> orderItemIds = new ArrayList<>();
        for (OrderItem item : tempOrderItems) {
            OrderItem savedItem = orderItemRepository.save(item);
            orderItemIds.add(savedItem.getId());
        }

        Order order = new Order(null, customerName, tableId, null, orderItemIds, LocalDateTime.now(), Status.PENDING);
        Order savedOrder = orderRepository.save(order);

        // Xóa dữ liệu tạm trong session sau khi xác nhận
        session.removeAttribute(key);

        return savedOrder;
    }

    public List<OrderItem> getTempOrderItems(String tableId) {
        HttpSession session = getSession();
        String key = TEMP_ORDER_ITEMS_KEY + "_" + tableId;
        List<OrderItem> tempOrderItems = (List<OrderItem>) session.getAttribute(key);
        return tempOrderItems != null ? tempOrderItems : new ArrayList<>();
    }
}
