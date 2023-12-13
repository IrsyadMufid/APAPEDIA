package com.apapedia.frontend.service;

import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.springframework.ui.Model;

import com.apapedia.frontend.dto.catalog.response.DetailCatalogResponseDTO;
import com.apapedia.frontend.dto.catalog.response.UpdateCatalogResponseDTO;

public interface CatalogService {
    List<Map<String, Object>> getAllCategories();
    void prepareAddCatalogForm(Model model, UUID activeUserId);
    List<Map<String, Object>> getAllCatalogs(UUID activeUserId);
    List<Map<String, Object>> getAllCatalogsBySeller(UUID sellerId);
    UpdateCatalogResponseDTO getCatalogById(UUID id);
    void prepareUpdateCatalogForm(UUID id, Model model, UUID activeUserId);
    List<Map<String, Object>> searchCatalogsByName(String productName);
    DetailCatalogResponseDTO getCatalogDetailById(UUID id);
    List<Map<String, Object>> filterCatalogsByPrice(Integer minPrice, Integer maxPrice);
    List<Map<String, Object>> sortCatalogs(String sortBy, String sortOrder);
}
