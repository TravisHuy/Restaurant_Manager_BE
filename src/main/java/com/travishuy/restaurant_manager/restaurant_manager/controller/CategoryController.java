package com.travishuy.restaurant_manager.restaurant_manager.controller;

import com.travishuy.restaurant_manager.restaurant_manager.model.Category;
import com.travishuy.restaurant_manager.restaurant_manager.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
/**
 * Controller class for handling category requests
 *
 * @version 0.1
 * @since 18-02-2025
 * @author TravisHuy
 */
@RestController
@RequestMapping("/api/category")
public class CategoryController {

    @Autowired
    CategoryService categoryService;

    /**
     * Adds a new category to the system
     *
     * @param category the category to add
     * @return ResponseEntity with the created category
     */
    @PostMapping("/add")
    public ResponseEntity<Category> addCategory(Category category){
        return ResponseEntity.ok(categoryService.addCategory(category));
    }
}
