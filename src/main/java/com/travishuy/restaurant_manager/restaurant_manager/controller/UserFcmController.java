package com.travishuy.restaurant_manager.restaurant_manager.controller;

import com.travishuy.restaurant_manager.restaurant_manager.service.UserFcmTokenService;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/user")
public class UserFcmController {

    @Autowired
    private UserFcmTokenService userFcmTokenService;

    /**
     * Register FCM token for the authenticated user
     */
    @PostMapping("/fcm-token")
    public ResponseEntity<Void> updateFcmToken(@RequestBody FcmTokenRequest request) {
        userFcmTokenService.updateFcmToken(request.getToken());
        return ResponseEntity.ok().build();
    }

    @Data
    static class FcmTokenRequest {
        private String token;
    }
}
