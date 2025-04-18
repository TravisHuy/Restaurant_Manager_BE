package com.travishuy.restaurant_manager.restaurant_manager.service;

import com.travishuy.restaurant_manager.restaurant_manager.model.AdminNotification;
import com.travishuy.restaurant_manager.restaurant_manager.model.Invoice;
import com.travishuy.restaurant_manager.restaurant_manager.model.NotificationType;
import com.travishuy.restaurant_manager.restaurant_manager.model.Order;
import com.travishuy.restaurant_manager.restaurant_manager.repository.AdminNotificationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Service class for managing admin notifications.
 * This class handles the creation, retrieval, and sending of notifications to the admin.
 *
 * @version 0.1
 * @since 16-04-2025
 * @author TravisHuy
 */
@Service
public class AdminNotificationService {

    @Autowired
    private AdminNotificationRepository notificationRepository;

    @Autowired
    private KafkaTemplate<String, AdminNotification> kafkaTemplate;

    private static final String TOPIC = "admin-notifications";

    /**
     * Thong bao khi thanh toan hoan thanh
     */
    public void notifyPaymentCompleted(Invoice invoice, Order order){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm dd/MM/yyyy");
        String formattedTime = invoice.getPaymentTime().format(formatter);

        String title = "Thanh toán hoàn thành";
        String message = String.format("Đơn hàng #%s đã được thanh toán thành công với số tiền %,.0f VNĐ vào lúc %s"
                ,order.getId(),invoice.getTotalAmount(),formattedTime);

        AdminNotification notification = new AdminNotification();
        notification.setTitle(title);
        notification.setMessage(message);
        notification.setType(NotificationType.PAYMENT);
        notification.setRelatedId(invoice.getId());
        notification.setRead(false);
        notification.setTimestamp(LocalDateTime.now());

        AdminNotification savedNotification = notificationRepository.save(notification);

        kafkaTemplate.send(TOPIC,savedNotification);

    }
}
