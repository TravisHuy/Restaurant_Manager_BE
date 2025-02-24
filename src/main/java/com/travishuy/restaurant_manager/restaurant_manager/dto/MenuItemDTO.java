package com.travishuy.restaurant_manager.restaurant_manager.dto;

import com.travishuy.restaurant_manager.restaurant_manager.model.MenuItem;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

@Data
public class MenuItemDTO {
    private String id;
    private String name;
    private String description;
    private double price;
    private String imageData;
    private String categoryId;
    private boolean available = true;
    private String note;

    public static MenuItemDTO fromMenuItem(MenuItem menuItem){
        MenuItemDTO menuItemDTO = new MenuItemDTO();
        menuItemDTO.setId(menuItem.getId());
        menuItemDTO.setName(menuItem.getName());
        menuItemDTO.setDescription(menuItem.getDescription());
        menuItemDTO.setPrice(menuItem.getPrice());
        menuItemDTO.setImageData(menuItem.getImageData());
        menuItemDTO.setCategoryId(menuItem.getCategoryId());
        menuItemDTO.setAvailable(menuItem.isAvailable());
        menuItemDTO.setNote(menuItem.getNote());
        return menuItemDTO;
    }

    public MenuItem toMenuItem(){
        MenuItem menuItem = new MenuItem();
        menuItem.setId(this.getId());
        menuItem.setName(this.getName());
        menuItem.setDescription(this.getDescription());
        menuItem.setPrice(this.getPrice());
        menuItem.setImageData(this.getImageData());
        menuItem.setCategoryId(this.getCategoryId());
        menuItem.setAvailable(this.isAvailable());
        menuItem.setNote(this.getNote());

        return menuItem;
    }
}
