package com.travishuy.restaurant_manager.restaurant_manager.service;

import com.mongodb.client.gridfs.model.GridFSFile;
import com.travishuy.restaurant_manager.restaurant_manager.dto.MenuItemDTO;
import com.travishuy.restaurant_manager.restaurant_manager.model.MenuItem;
import com.travishuy.restaurant_manager.restaurant_manager.repository.CategoryRepository;
import com.travishuy.restaurant_manager.restaurant_manager.repository.MenuItemRepository;
import lombok.RequiredArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.gridfs.GridFsOperations;
import org.springframework.data.mongodb.gridfs.GridFsResource;
import org.springframework.data.mongodb.gridfs.GridFsTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Base64;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Represents the service for the menu items.
 * This class is used to create new item ,....
 *
 * @version 0.1
 * @since 18-02-2025
 * @author TravisHuy
 */
@Service
@RequiredArgsConstructor
public class MenuItemService {

    @Autowired
    MenuItemRepository menuItemRepository;

    @Autowired
    CategoryRepository categoryRepository;

    @Autowired
    GridFsTemplate gridFsTemplate;

    @Autowired
    GridFsOperations gridFsOperations;

    /**
     * Save a new menu item with image
     * @param menuItemDTO
     * @param imageFile
     * @return
     * @throws IOException
     */
    public MenuItemDTO createMenuItem(MenuItemDTO menuItemDTO,MultipartFile imageFile,String categoryId) throws IOException {

       //Convert DTO to entity
        MenuItem menuItem = menuItemDTO.toMenuItem();

        if(!categoryRepository.existsById(categoryId)){
            throw new IllegalArgumentException("Category not found");
        }

        if(menuItemRepository.existsByName(menuItemDTO.getName())){
            throw new IllegalArgumentException("Menu item name already exists");
        }

        MenuItem savedMenuItem = menuItemRepository.save(menuItem);

        // store the image in GridFS if an image is provided
        if(imageFile != null && !imageFile.isEmpty()){
            String imageBase64 = Base64.getEncoder().encodeToString(imageFile.getBytes());
            savedMenuItem.setImageData(imageBase64);

            savedMenuItem = menuItemRepository.save(savedMenuItem);
        }

        return MenuItemDTO.fromMenuItem(savedMenuItem);
    }

    /**
     * Update an existing menu item image
     *
     * @param id menu item dto
     * @param menuItemDTO update menu item dto
     * @param imageFile
     * @return
     * @throws IOException
     */
    public MenuItemDTO updateMenuItem(String id, MenuItemDTO menuItemDTO, MultipartFile imageFile) throws IOException {
        MenuItem existingItem = menuItemRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Menu item not found with id + " + id));

        existingItem.setName(menuItemDTO.getName());
        existingItem.setPrice(menuItemDTO.getPrice());
        existingItem.setDescription(menuItemDTO.getDescription());
        existingItem.setAvailable(menuItemDTO.isAvailable());
        existingItem.setCategoryId(menuItemDTO.getCategoryId());

        if(imageFile!=null && !imageFile.isEmpty()){
            String imageBase64 = Base64.getEncoder().encodeToString(imageFile.getBytes());

            existingItem.setImageData(imageBase64);
        }

        MenuItem updateMenuItem = menuItemRepository.save(existingItem);

        return MenuItemDTO.fromMenuItem(updateMenuItem);
    }


    public List<MenuItemDTO> getAllMenuItems(){
        return menuItemRepository.findAll().stream()
                .map(MenuItemDTO::fromMenuItem).collect(Collectors.toList());
    }

}
