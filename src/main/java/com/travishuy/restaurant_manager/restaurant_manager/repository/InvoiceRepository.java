package com.travishuy.restaurant_manager.restaurant_manager.repository;

import com.travishuy.restaurant_manager.restaurant_manager.model.Invoice;
import com.travishuy.restaurant_manager.restaurant_manager.model.PaymentMethod;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface InvoiceRepository extends MongoRepository<Invoice, String> {
    /**
     * Find an invoice by its order ID
     * @param orderId the ID of the associated order
     * @return Optional containing the invoice if found, empty otherwise
     */
    Optional<Invoice> findByOrderId(String orderId);
    /**
     * Find invoices by payment method
     * @param paymentMethod the payment method used
     * @return List of invoices using the specified payment method
     */
    List<Invoice> findByPaymentMethod(PaymentMethod paymentMethod);

    /**
     * Find invoices created between two dates
     * @param startDate the start date
     * @param endDate the end date
     * @return List of invoices created within the date range
     */
    List<Invoice> findByPaymentTimeBetween(LocalDateTime startDate, LocalDateTime endDate);
}
