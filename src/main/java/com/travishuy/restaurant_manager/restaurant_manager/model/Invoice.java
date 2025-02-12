package com.travishuy.restaurant_manager.restaurant_manager.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
/**
 * Represents an invoice in the restaurant system
 * This class maps to the "invoices" collection in MongoDB database
 *
 * @version 0.1
 * @since 12-02-2025
 * @author TravisHuy
 */
@Document(collection = "invoices")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Invoice {
    /** Unique identifier for the invoice */
    @Id
    private String id;
    /** Reference to the order */
    private String orderId;
    /** Total amount of the invoice */
    private double totalAmount;
    /** Date and time of the payment */
    private LocalDateTime paymentTime;
    /** Payment method used */
    private PaymentMethod paymentMethod;
}
