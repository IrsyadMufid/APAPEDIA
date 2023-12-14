package com.apapedia.catalog.controller;
import com.apapedia.catalog.dto.ShowCategoryRequestDTO;
import com.apapedia.catalog.mapper.CategoryMapper;
import com.apapedia.catalog.model.Category;
import com.apapedia.catalog.service.CategoryService;
import jakarta.transaction.Transactional;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@CrossOrigin
@Transactional
@RestController
@RequestMapping("/category")
public class CategoryController {
    @Autowired
    private CategoryService categoryService;
    @Autowired
    private CategoryMapper categoryMapper;

    @GetMapping("/all")
    public List<ShowCategoryRequestDTO> getAllCategories() {
        List<Category> categories = categoryService.getAllCategories();
        return categories.stream()
                .map(categoryMapper::categoryToCategoryResponseDTO)
                .collect(Collectors.toList());
    }
}