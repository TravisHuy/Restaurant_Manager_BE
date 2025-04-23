package com.travishuy.restaurant_manager.restaurant_manager.controller;

import com.travishuy.restaurant_manager.restaurant_manager.model.Invoice;
import com.travishuy.restaurant_manager.restaurant_manager.model.Order;
import com.travishuy.restaurant_manager.restaurant_manager.model.PaymentMethod;
import com.travishuy.restaurant_manager.restaurant_manager.model.Status;
import com.travishuy.restaurant_manager.restaurant_manager.service.AdminNotificationService;
import com.travishuy.restaurant_manager.restaurant_manager.service.InvoiceService;
import com.travishuy.restaurant_manager.restaurant_manager.service.OrderService;
import com.travishuy.restaurant_manager.restaurant_manager.service.VnPayService;
import com.travishuy.restaurant_manager.restaurant_manager.util.QrCodeGenerator;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/payments")
public class PaymentController {

    @Autowired
    private OrderService orderService;
    @Autowired
    private InvoiceService invoiceService;
    @Autowired
    private VnPayService vnPayService;
    @Autowired
    private QrCodeGenerator qrCodeGenerator;
    @Autowired
    private AdminNotificationService notificationService;

    /**
     * Tao URl thanh toan VNPay thong thuong
     */
    @PostMapping("/vnpay/create/{orderId}")
    public ResponseEntity<?> createVnPayment(@PathVariable String orderId, HttpServletRequest request){
        try{
            Order order = orderService.getOrderId(orderId);
            if(order.getStatus() != Status.COMPLETED){
                return ResponseEntity.badRequest().body("Đơn hàng chưa hoàn thanh , không the thanh toán");
            }

            String ipAddress = request.getRemoteAddr();
            String paymentUrl = vnPayService.createPaymentUrl(order, ipAddress);


            Map<String, Object> response = new HashMap<>();
            response.put("paymentUrl" , paymentUrl);
            response.put("orderId", orderId);

            return ResponseEntity.ok(response);
        }
        catch (Exception e){
            return ResponseEntity.badRequest().body("Lỗi tạo URL thanh toán: "+ e.getMessage());
        }
    }

    /**
     * Tao Qr code thanh toan VNPay
     */
    @PostMapping("/vnpay/qr/{orderId}")
    public ResponseEntity<?> createVnPayQrPayment(@PathVariable String orderId, HttpServletRequest request){
        try{
            Order order = orderService.getOrderId(orderId);
            if(order.getStatus() == Status.CANCELLED){
                return ResponseEntity.badRequest().body("Don hang chua hoan thanh , khong the thanh toan");
            }
            String ipAddress = request.getRemoteAddr();
            String paymentUrl = vnPayService.createQrPaymentUrl(order,ipAddress);

            String qrCodeImage = qrCodeGenerator.generateQrCodeBase64(paymentUrl,300,300);

            Map<String , Object> response = new HashMap<>();
            response.put("qrCodeImage",qrCodeImage);
            response.put("paymentUrl", paymentUrl);
            response.put("orderId", orderId);
            response.put("amount",order.getTotalAmount());
            response.put("customerName", order.getCustomerName());
            response.put("instructions", "Quet ma Qr bang ung dung ngan hang hoac VNPay de thanh toan");

            return ResponseEntity.ok(response);
        }
        catch (Exception e){
            return ResponseEntity.badRequest().body("Loi tao ma qr thanh toan "+ e.getMessage());
        }
    }

    /**
     * Tao Qr code thanh toan VNPay
     */
    @PostMapping("/vnpay/paymentTest/qr/{orderId}")
    public ResponseEntity<?> createVnPayTestQrPayment(@PathVariable String orderId, HttpServletRequest request){
        try{
            Order order = orderService.getOrderId(orderId);
            if(order.getStatus() != Status.CANCELLED){
                return ResponseEntity.badRequest().body("Don hang chua hoan thanh , khong the thanh toan");
            }
            String ipAddress = request.getRemoteAddr();
            String paymentUrl = vnPayService.createTestPaymentUrl(order,ipAddress);

            Map<String , Object> response = new HashMap<>();
            response.put("paymentUrl", paymentUrl);
            response.put("orderId", orderId);
            response.put("testCardInfo",getTestCardInfo());
            return ResponseEntity.ok(response);
        }
        catch (Exception e){
            return ResponseEntity.badRequest().body("Loi tao ma qr thanh toan "+ e.getMessage());
        }
    }

    /**
     * Xu ly callback tu VNPay
     */
    @GetMapping("/vnpay/callback")
    public ResponseEntity<?> vnpayCallback(@RequestParam Map<String,String> params){
        try {
           if(!vnPayService.validatePaymentResponse(params)){
               return ResponseEntity.badRequest().body("Xac thuc thanh toan that bai");
           }
            String vnpTxnRef = params.get("vnp_TxnRef");
            String orderId = vnpTxnRef.split("-")[0];;
            double amount = Double.parseDouble(params.get("vnp_Amount")) / 100;

            Order order = orderService.getOrderId(orderId);
            Invoice invoice = invoiceService.createInvoice(orderId, amount, PaymentMethod.ONLINE);

            // Gửi thông báo cho admin
            notificationService.notifyPaymentCompleted(invoice, order);

            Map<String, Object> response = new HashMap<>();
            response.put("status", "success");
            response.put("message", "Thanh toán thành công");
            response.put("invoiceId", invoice.getId());
            response.put("amount", amount);
            response.put("paymentTime", invoice.getPaymentTime());

            return ResponseEntity.ok(response);

        }
        catch (Exception e) {
            return ResponseEntity.badRequest().body("Xử lý thanh toán thất bại: " + e.getMessage());
        }
    }
    /**
     * Thong tin the test cho moi truong sanbox
     * @return
     */
    private Map<String,String> getTestCardInfo() {
        Map<String,String>  testCard = new HashMap<>();
        testCard.put("bankCode", "NCB");
        testCard.put("cardNumber", "9704198526191432198");
        testCard.put("cardName", "NGUYEN VAN A");
        testCard.put("expiryDate", "07/15");
        testCard.put("otp", "123456");
        return testCard;
    }

}
