package com.travishuy.restaurant_manager.restaurant_manager.service;

import com.travishuy.restaurant_manager.restaurant_manager.model.Invoice;
import com.travishuy.restaurant_manager.restaurant_manager.model.PaymentMethod;
import com.travishuy.restaurant_manager.restaurant_manager.repository.InvoiceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * Service interface for managing Invoice operations
 *
 * @author Travis Huy
 * @version 0.1
 * @since 08-03-2025
 */
@Service
public interface InvoiceService {

    /**
     * Create a new invoice for an order
     * @param orderId ID of the order to create invoice for
     * @param totalAmount total amount to be paid
     * @param paymentMethod method of payment
     * @return the newly created invoice
     */
    Invoice createInvoice(String orderId, double totalAmount, PaymentMethod paymentMethod);

    /**
     * Get all invoices in the system
     *
     * @return list of all invoices
     */
    List<Invoice> getAllInvoices();

    /**
     * Get an invoice by its ID
     * @param id ID of the invoice to retrieve
     * @return Optional containing the invoice if found
     */
    Optional<Invoice> getInvoiceById(String id);

    /**
     * Get an invoice by its associated order ID
     * @param orderId ID of the order
     * @return Optional containing the invoice if found
     */
    Optional<Invoice> getInvoiceByOrderId(String orderId);

    /**
     * Calculate revenue for a specific time period
     * @param startDate beginning of date range
     * @param endDate end of date range
     * @return total revenue for the period
     */
    double calculateRevenue(LocalDateTime startDate, LocalDateTime endDate);

    /**
     * Get all invoices with pagination support
     * @param pageable pagination information
     * @return list of invoices with pagination
     */
    List<Invoice> getAllInvoicesWithPagination(Pageable pageable);

    /**
     * Get all invoices by payment method
     * @param paymentMethod payment method to filter invoices
     * @return list of invoices with the specified payment method
     */
    List<Invoice> getInvoicesByPaymentMethod(PaymentMethod paymentMethod);

    /**
     * Get all invoices by date range
     * @param query date range to filter invoices
     * @return list of invoices within the specified date range
     */
    List<Invoice> searchInvoices(String query);
}
