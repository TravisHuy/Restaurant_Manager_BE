package com.travishuy.restaurant_manager.restaurant_manager.controller;

import com.travishuy.restaurant_manager.restaurant_manager.model.Category;
import com.travishuy.restaurant_manager.restaurant_manager.service.CategoryService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
    @PreAuthorize("hasAnyRole('ADMIN','MANAGER')")
    public ResponseEntity<Category> addCategory(@Valid @RequestBody Category category){
        return ResponseEntity.ok(categoryService.addCategory(category));
    }
    /**
     * Retrieves all categories in the system
     *
     * @return ResponseEntity with a list of all categories
     */
    @GetMapping("/all")
    public ResponseEntity<List<Category>> getAllCategories(){
        return ResponseEntity.ok(categoryService.getAllCategories());
    }
    /**
     * Retrieves a category by its id
     *
     * @param id the id of the category
     * @return ResponseEntity with the category
     */
    @GetMapping("/{id}")
    public ResponseEntity<Category> getCategoryById(@PathVariable String id) {
        return ResponseEntity.ok(categoryService.getCategoryById(id));
    }
}
