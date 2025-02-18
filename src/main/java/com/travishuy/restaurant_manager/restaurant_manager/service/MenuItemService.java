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
import org.springframework.data.mongodb.gridfs.GridFsTemplate;
import org.springframework.stereotype.Service;

import java.io.IOException;

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

    public MenuItem createMenuItem(MenuItemDTO menuItemDTO,String categoryId) throws IOException {
        ObjectId imageId = null;

        if(!categoryRepository.existsById(categoryId)){
            throw new IllegalArgumentException("Category not found");
        }

        if(menuItemDTO.getImage() != null && !menuItemDTO.getImage().isEmpty()){
            imageId = gridFsTemplate.store(
                    menuItemDTO.getImage().getInputStream(),
                    menuItemDTO.getImage().getOriginalFilename(),
                    menuItemDTO.getImage().getContentType()
            );
        }

        // create a new MenuItem object
        MenuItem menuItem = new MenuItem();
        menuItem.setName(menuItemDTO.getName());
        menuItem.setDescription(menuItemDTO.getDescription());
        menuItem.setPrice(menuItemDTO.getPrice());
        menuItem.setCategoryId(categoryId);
        menuItem.setAvailable(menuItemDTO.isAvailable());
        menuItem.setImageId(imageId);

        return menuItemRepository.save(menuItem);
    }
    public byte[] getImage(String id) throws IOException {
        MenuItem menuItem = menuItemRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Menu item not found"));

        if(menuItem.getImageId() == null) {
            throw new IllegalArgumentException("Image not found for this menu item");
        }
        GridFSFile file = gridFsTemplate.findOne(new Query(Criteria.where("_id").is(menuItem.getImageId())));

        if(file == null) {
            throw new IllegalArgumentException("Image file not found in GridFs");
        }

        return gridFsOperations.getResource(file).getInputStream().readAllBytes();
    }


}
