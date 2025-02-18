package com.travishuy.restaurant_manager.restaurant_manager.dto;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

@Data
public class MenuItemDTO {
    private String name;
    private String description;
    private double price;
    private String categoryId;
    private boolean available;
    private MultipartFile image;
}
