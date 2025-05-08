package com.travishuy.restaurant_manager.restaurant_manager.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.travishuy.restaurant_manager.restaurant_manager.model.AdminNotification;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

@Service
public class KafkaNotificationListener {

    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    private final ObjectMapper objectMapper = new ObjectMapper().registerModule(new JavaTimeModule())
            .configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS,false);

    @KafkaListener(topics = "admin-notifications")
    public void listen(String message){
        try{
            AdminNotification notification = objectMapper.readValue(message, AdminNotification.class);
            // Forward to WebSocket clients
            messagingTemplate.convertAndSend("/topic/admin-notifications", notification);
        }
        catch(JsonProcessingException e){
            System.err.println("Error processing Kafka message: " + e.getMessage());
        }
    }

}
