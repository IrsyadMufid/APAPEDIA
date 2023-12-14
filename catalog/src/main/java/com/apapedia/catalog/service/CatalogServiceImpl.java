package com.apapedia.catalog.service;

import com.apapedia.catalog.dto.UpdateCatalogRequestDTO;
import com.apapedia.catalog.mapper.CatalogMapper;
import com.apapedia.catalog.model.Catalog;
import com.apapedia.catalog.repository.CatalogDb;

import jakarta.transaction.Transactional;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.data.domain.Sort;


@Service
@Transactional
public class CatalogServiceImpl implements CatalogService {

    @Autowired
    private CatalogDb catalogDb;

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private CatalogMapper catalogMapper;    

    @Override
    public Catalog addCatalog(Catalog catalog) {
        return catalogDb.save(catalog);
    }

    @Override
    public List<Catalog> findAllSortedByName() {
        return catalogDb.findAllByOrderByProductNameAsc();
    }

    @Override
    public Optional<Catalog> findCatalogById(UUID id) {
        return catalogDb.findById(id);
    }

    @Override
    public Catalog updateCatalog(UUID id, UpdateCatalogRequestDTO updateCatalogRequestDTO, byte[] imageContent) {
        var catalog = catalogDb.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Catalog not found"));
    
        // Fetch the existing Category from the database or create a new one if not found
        var existingCategory = categoryService.getOrCreateCategoryByName(updateCatalogRequestDTO.getCategoryId());
    
        // Update Catalog entity with the existing Category
        catalog.setCategory(existingCategory);
    
        // Mapping other data from DTO to Catalog
        catalog = catalogMapper.updateCatalogRequestDTOToCatalog(updateCatalogRequestDTO, catalog);
    
        // Set the image content if provided
        if (imageContent != null) {
            catalog.setImage(imageContent);
        }
    
        return catalogDb.save(catalog);
    }
    

    @Override
    public void softDeleteCatalog(UUID id) {
        var catalog = catalogDb.findById(id).orElse(null);
        if (catalog != null) {
            catalog.setIsDeleted(true);
            catalogDb.save(catalog);
        }
    }
    

    @Override
    public List<Catalog> findCatalogsByProductName(String productName) {
        return catalogDb.findByProductNameContainingIgnoreCase(productName);
    }

    @Override
    public List<Catalog> findCatalogsBySellerId(UUID sellerId) {
        return catalogDb.findBySellerId(sellerId);
    }

    @Override
    public List<Catalog> findCatalogsByPriceRange(Integer minPrice, Integer maxPrice) {
        return catalogDb.findByPriceBetween(minPrice, maxPrice);
    }

    @Override
    public List<Catalog> findAllSorted(String sortBy, String sortOrder) {
        var sort = Sort.by(sortOrder.equals("asc") ? Sort.Direction.ASC : Sort.Direction.DESC, sortBy);
        return catalogDb.findAll(sort);
    }
    

    @Override
    public byte[] processFile(MultipartFile file) throws IOException {
    // Check if the file is not empty
    if (file.isEmpty()) {
        throw new IllegalArgumentException("File is empty");
    }

    // Check if the file is an image (you can customize this check based on your requirements)
    if (!isImage(file)) {
        throw new IllegalArgumentException("File is not an image");
    }

    // Convert MultipartFile to byte[]
    return file.getBytes();
}

    private boolean isImage(MultipartFile file) {
        // Implement the logic to check if the file is an image
        // You can use libraries like Apache Tika or simply check the file extension
        // For simplicity, let's assume any file with ".jpg", ".jpeg", or ".png" extension is considered an image
        String fileName = file.getOriginalFilename();
        return fileName != null && (fileName.endsWith(".jpg") || fileName.endsWith(".jpeg") || fileName.endsWith(".png"));
    }
}
