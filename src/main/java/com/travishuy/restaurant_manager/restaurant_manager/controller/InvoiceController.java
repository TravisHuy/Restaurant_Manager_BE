package com.travishuy.restaurant_manager.restaurant_manager.controller;

import com.travishuy.restaurant_manager.restaurant_manager.model.PaymentMethod;
import com.travishuy.restaurant_manager.restaurant_manager.service.InvoiceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/invoices")
public class InvoiceController {

    @Autowired
    InvoiceService invoiceService;

    @PostMapping("/add")
    @PreAuthorize("hasAnyRole('ADMIN','MANAGER')")
    public ResponseEntity<?> createInvoice(@RequestBody Map<String,Object> payload){
        try {
            String orderId = (String) payload.get("orderId");
            double totalAmount = (double) payload.get("totalAmount");
            PaymentMethod paymentMethod = PaymentMethod.valueOf((String) payload.get("paymentMethod"));
            return ResponseEntity.ok(invoiceService.createInvoice(orderId, totalAmount, paymentMethod));
        }catch (IllegalArgumentException | IllegalStateException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/all")
    public ResponseEntity<?> getAllInvoices(){
        try {
            return ResponseEntity.ok(invoiceService.getAllInvoices());
        }catch (IllegalArgumentException e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
