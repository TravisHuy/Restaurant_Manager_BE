package com.travishuy.restaurant_manager.restaurant_manager.service;

import com.travishuy.restaurant_manager.restaurant_manager.model.Order;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Service class for handling VnPay payment operations
 *
 * @version 0.1
 * @since 16-04-2025
 * @author TravisHuy
 */
@Service
public class VnPayService {

    @Value("${vnpay.terminal-id}")
    private String vnpTerminalId;

    @Value("${vnpay.secret-key}")
    private String vnpSecretKey;

    @Value("${vnpay.return-url}")
    private String vnpReturnUrl;

    @Value("${vnpay.api-url}")
    private String vnpApiUrl;

    /**
     * Generates a payment URL for VnPay
     */
    public String createPaymentUrl(Order order, String ipAddress){
       String vnpTxnRef = order.getId() + "-" + System.currentTimeMillis();
       String vnpAmount = String.valueOf((int)(order.getTotalAmount()*100));

        Map<String,String> vnpParams = new HashMap<>() ;
        vnpParams.put("vnp_Version", "2.1.0");
        vnpParams.put("vnp_Command", "pay");
        vnpParams.put("vnp_TmnCode", vnpTerminalId);
        vnpParams.put("vnp_Amount", vnpAmount);
        vnpParams.put("vnp_CurrCode", "VND");
        vnpParams.put("vnp_TxnRef", vnpTxnRef);
        vnpParams.put("vnp_OrderInfo", "Thanh toán đơn hàng " + order.getId());
        vnpParams.put("vnp_OrderType","food");
        vnpParams.put("vnp_Locale", "vn");
        vnpParams.put("vnp_ReturnUrl",vnpReturnUrl);
        vnpParams.put("vn_IpAddr",ipAddress);
        vnpParams.put("vnp_CreateDate",getCurrentDate());

        String secureHash = calculateHmacSHA512(vnpParams);
        vnpParams.put("vnp_SecureHash",secureHash);

        return  buildUrlFromParams(vnpApiUrl,vnpParams);
    }


    /**
     * Generates a payment URL for VnPay
     */
    public String createQrPaymentUrl(Order order, String ipAddress){
        String vnpTxnRef = order.getId() + "-" + System.currentTimeMillis();
        String vnpAmount = String.valueOf((int)(order.getTotalAmount()*100));

        Map<String,String> vnpParams = new HashMap<>() ;
        vnpParams.put("vnp_Version", "2.1.0");
        vnpParams.put("vnp_Command", "pay");
        vnpParams.put("vnp_TmnCode", vnpTerminalId);
        vnpParams.put("vnp_Amount", vnpAmount);
        vnpParams.put("vnp_CurrCode", "VND");
        vnpParams.put("vnp_TxnRef", vnpTxnRef);
        vnpParams.put("vnp_OrderInfo", "Thanh toán đơn hàng " + order.getId());
        vnpParams.put("vnp_OrderType","food");
        vnpParams.put("vnp_Locale", "vn");
        vnpParams.put("vnp_ReturnUrl",vnpReturnUrl);
        vnpParams.put("vn_IpAddr",ipAddress);
        vnpParams.put("vnp_CreateDate",getCurrentDate());
        vnpParams.put("vnp_BankCode","VNPAYQR");

        String secureHash = calculateHmacSHA512(vnpParams);
        vnpParams.put("vnp_SecureHash",secureHash);

        return  buildUrlFromParams(vnpApiUrl,vnpParams);
    }
    /**
     * Tao url thanh toan qr code cho sandbox test
     */
    public String createTestPaymentUrl(Order order,String ipAddress){
        String vnpTxnRef = order.getId() + "-" + System.currentTimeMillis();
        String vnpAmount = String.valueOf((int)(order.getTotalAmount() * 100));

        Map<String, String> vnpParams = new HashMap<>();
        vnpParams.put("vnp_Version", "2.1.0");
        vnpParams.put("vnp_Command", "pay");
        vnpParams.put("vnp_TmnCode", vnpTerminalId);
        vnpParams.put("vnp_Amount", vnpAmount);
        vnpParams.put("vnp_CurrCode", "VND");
        vnpParams.put("vnp_TxnRef", vnpTxnRef);
        vnpParams.put("vnp_OrderInfo", "Thanh toan don hang " + order.getId());
        vnpParams.put("vnp_OrderType", "food");
        vnpParams.put("vnp_Locale", "vn");
        vnpParams.put("vnp_ReturnUrl", vnpReturnUrl);
        vnpParams.put("vnp_IpAddr", ipAddress);
        vnpParams.put("vnp_CreateDate", getCurrentDate());

        vnpParams.put("vnp_BankCode", "VNBANK");

        String secureHash = calculateHmacSHA512(vnpParams);
        vnpParams.put("vnp_SecureHash", secureHash);

        return buildUrlFromParams(vnpApiUrl, vnpParams);

    }
    /**
     * Tinh toan HMAC SHA-512 bao mat
     */
    private String calculateHmacSHA512(Map<String,String> params){
        List<String> fieldNames = new ArrayList<>(params.keySet());
        Collections.sort(fieldNames);

        StringBuilder hashData = new StringBuilder();
        for(String fieldName: fieldNames){
            String fieldValue = params.get(fieldName);
            if(fieldValue != null && !fieldValue.isEmpty()){
                hashData.append(fieldName).append("=").append(fieldValue).append("&");
            }
        }

        String data = hashData.substring(0,hashData.length() -1);

        try {
            Mac sha512Hmac = Mac.getInstance("HmacSHA512");
            SecretKeySpec secretKey = new SecretKeySpec(vnpSecretKey.getBytes(StandardCharsets.UTF_8), "HmacSHA512");
            sha512Hmac.init(secretKey);
            byte[] hmacBytes = sha512Hmac.doFinal(data.getBytes(StandardCharsets.UTF_8));

            // Convert bytes to hex
            StringBuilder hexString = new StringBuilder();
            for (byte hmacByte : hmacBytes) {
                String hex = Integer.toHexString(0xff & hmacByte);
                if (hex.length() == 1) hexString.append('0');
                hexString.append(hex);
            }

            return hexString.toString().toUpperCase();
        } catch (Exception e) {
            throw new RuntimeException("Không thể tạo HMAC SHA-512", e);
        }

    }

    /**
     * Xay dung url tu cac tham so
     */
    private String buildUrlFromParams(String baseUrl,Map<String,String> params){
        StringBuilder paymentUrl = new StringBuilder(baseUrl).append("?");
        for(Map.Entry<String,String> entry: params.entrySet()){
            paymentUrl.append(entry.getKey()).append("=")
                    .append(URLEncoder.encode(entry.getValue(),StandardCharsets.UTF_8))
                    .append("&");
        }
        return paymentUrl.substring(0, paymentUrl.length()-1);

    }
    /**
     * Lay dinh dang ngay hien tai cho VNPay
     */
    private String getCurrentDate() {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmss");
        return formatter.format(new Date());
    }

    /**
     * Xac thuc reponse tu Vnpay
     */
    public boolean validatePaymentResponse(Map<String,String> response){
        String secureHash = response.get("vnp_SecureHash");
        Map<String, String> validationParams = new HashMap<>();
        validationParams.remove("vnp_SecureHash");
        validationParams.remove("vnp_SecureHashType");

        String calculateHash = calculateHmacSHA512(validationParams);
        return secureHash.equals(calculateHash) && "00".equals(response.get("vnp_ResponseCode"));
    }


}
