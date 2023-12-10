package com.apapedia.catalog.service;

import com.apapedia.catalog.model.Category;

import jakarta.transaction.Transactional;

import java.util.List;

@Transactional
public interface CategoryService {
    Category addCategory(Category category);
    List<Category> getAllCategories();
    Category getCategoryById(Long id);
    public Category getOrCreateCategoryByName(String categoryName);
}
