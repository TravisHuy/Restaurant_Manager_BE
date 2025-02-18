package com.travishuy.restaurant_manager.restaurant_manager.service;

import com.travishuy.restaurant_manager.restaurant_manager.model.Category;
import com.travishuy.restaurant_manager.restaurant_manager.repository.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Represents the service for the category.
 * This class is used to add category.
 *
 * @version 0.1
 * @since 18-02-2025
 * @author TravisHuy
 */

@Service
public class CategoryService {

    @Autowired
    CategoryRepository categoryRepository;

    public Category addCategory(Category category){
        if(categoryRepository.existsByName(category.getName())){
            throw new IllegalArgumentException("Category name already exists");
        }
        Category newCategory = new Category();
        newCategory.setName(category.getName());

       return categoryRepository.save(newCategory);
    }
}
