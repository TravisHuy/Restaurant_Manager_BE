package com.travishuy.restaurant_manager.restaurant_manager.service;

import com.travishuy.restaurant_manager.restaurant_manager.model.*;
import com.travishuy.restaurant_manager.restaurant_manager.repository.InvoiceRepository;
import com.travishuy.restaurant_manager.restaurant_manager.repository.OrderRepository;
import com.travishuy.restaurant_manager.restaurant_manager.repository.TableRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class InvoiceServiceImpl implements InvoiceService {

    @Autowired
    InvoiceRepository invoiceRepository;
    @Autowired
    OrderRepository orderRepository;
    @Autowired
    TableRepository tableRepository;

    @Override
    public Invoice createInvoice(String orderId, double totalAmount, PaymentMethod paymentMethod) {
        Optional<Order> orderOptional = orderRepository.findById(orderId);

        if(orderOptional.isEmpty()) {
            throw new IllegalArgumentException("Order not found with ID: " + orderId);
        }
        Optional<Invoice> existingInvoice = invoiceRepository.findByOrderId(orderId);
        if (existingInvoice.isPresent()) {
            throw new IllegalStateException("Invoice already exists for order ID: " + orderId);
        }

        Order order = orderOptional.get();

        if(order.getStatus() != Status.IN_PROCESS) {
            throw new IllegalStateException("Order must be in process before creating invoice");
        }

        Invoice invoice = new Invoice();
        invoice.setOrderId(orderId);
        invoice.setTotalAmount(totalAmount);
        invoice.setPaymentTime(LocalDateTime.now());
        invoice.setPaymentMethod(paymentMethod);

        order.setStatus(Status.COMPLETED);

        String tableId = order.getTableId();
        if(tableId!=null && !tableId.isEmpty()) {
            Optional<Table> tableOptional = tableRepository.findById(tableId);
            if(tableOptional.isPresent()){
                Table table  = tableOptional.get();
                table.setAvailable(true);
                table.setReservationId(null);
                table.setOrderId(null);
                tableRepository.save(table);
            }
        }

        return invoiceRepository.save(invoice);
    }

    @Override
    public Invoice createInvoiceProvisional(String orderId, double totalAmount, PaymentMethod paymentMethod) {
        Optional<Order> orderOptional = orderRepository.findById(orderId);

        if(orderOptional.isEmpty()) {
            throw new IllegalArgumentException("Order not found with ID: " + orderId);
        }
        Optional<Invoice> existingInvoice = invoiceRepository.findByOrderId(orderId);
        if (existingInvoice.isPresent()) {
            throw new IllegalStateException("Invoice already exists for order ID: " + orderId);
        }

        Order order = orderOptional.get();

        if(order.getStatus() != Status.IN_PROCESS) {
            throw new IllegalStateException("Order must be in process before creating invoice");
        }

        Invoice invoice = new Invoice();
        invoice.setOrderId(orderId);
        invoice.setTotalAmount(totalAmount);
        invoice.setPaymentTime(LocalDateTime.now());
        invoice.setPaymentMethod(paymentMethod);

        order.setStatus(Status.PROVISIONAL);

        String tableId = order.getTableId();
        if(tableId!=null && !tableId.isEmpty()) {
            Optional<Table> tableOptional = tableRepository.findById(tableId);
            if(tableOptional.isPresent()){
                Table table  = tableOptional.get();
                table.setAvailable(true);
                table.setReservationId(null);
                table.setOrderId(null);
                tableRepository.save(table);
            }
        }

        return invoiceRepository.save(invoice);
    }


    @Override
    public List<Invoice> getAllInvoices() {
        return invoiceRepository.findAll();
    }

    @Override
    public Optional<Invoice> getInvoiceById(String id) {
        return Optional.empty();
    }

    @Override
    public Optional<Invoice> getInvoiceByOrderId(String orderId) {
        return Optional.empty();
    }

    @Override
    public double calculateRevenue(LocalDateTime startDate, LocalDateTime endDate) {
        return 0;
    }

    @Override
    public List<Invoice> getAllInvoicesWithPagination(Pageable pageable) {
        return invoiceRepository.findAll(pageable).getContent();
    }

    @Override
    public List<Invoice> getInvoicesByPaymentMethod(PaymentMethod paymentMethod) {
        return invoiceRepository.findByPaymentMethod(paymentMethod);
    }

    @Override
    public List<Invoice> searchInvoices(String query) {
        if(query == null || query.isEmpty()) {
            return new ArrayList<>();
        }

        List<Invoice> byId = invoiceRepository.findByIdContainingIgnoreCase(query);

        List<Invoice> byOrderId = invoiceRepository.findByOrderIdContainingIgnoreCase(query);

        return byId.stream()
                .distinct()
                .filter(invoice -> !byOrderId.contains(invoice))
                .collect(Collectors.toList());

    }
}
