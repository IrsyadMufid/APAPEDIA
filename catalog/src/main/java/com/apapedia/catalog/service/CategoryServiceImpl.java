package com.apapedia.catalog.service;

import com.apapedia.catalog.model.Category;
import com.apapedia.catalog.repository.CategoryDb;

import jakarta.transaction.Transactional;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
@Transactional
public class CategoryServiceImpl implements CategoryService {
    @Autowired
    private CategoryDb categoryDb;

    
    @Override
    public Category addCategory(Category category) {
        return categoryDb.save(category);
    }


    @Override
    public List<Category> getAllCategories() {
        return categoryDb.findAll();
    }

    @Override
    public Category getCategoryById(Long id) {
        // The repository method 'findById' returns an Optional,
        // so we use 'orElseThrow' to throw an exception if the category is not found.
        return categoryDb.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Category not found with id: " + id));
    }

    @Override
    public Category getOrCreateCategoryByName(String categoryId) {
        // Directly return the category based on the provided categoryId
        return categoryDb.findById(Long.parseLong(categoryId))
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Category not found with id: " + categoryId));
    }
}
