package com.apapedia.catalog.service;

import com.apapedia.catalog.model.Category;
import com.apapedia.catalog.repository.CategoryDb;
import com.apapedia.catalog.service.CategoryService;

import jakarta.transaction.Transactional;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service

public class CategoryServiceImpl implements CategoryService {

    @Autowired
    private CategoryDb categoryDb;

    
    @Override
    public Category addCategory(Category category) {
        // Check if category with the same name already exists to prevent duplicates
        // This step is optional and depends on your business logic
        Optional<Category> existingCategory = categoryDb.findByName(category.getName());
        if (existingCategory.isPresent()) {
            return existingCategory.get(); // Or handle this case as your logic dictates
        }
        // Save the new category to the database and return it
        return categoryDb.save(category);
    }

    
}
