package com.apapedia.catalog.controller;

import com.apapedia.catalog.dto.CreateCatalogRequestDTO;
import com.apapedia.catalog.dto.ShowCatalogRequestDTO;
import com.apapedia.catalog.dto.UpdateCatalogRequestDTO;
import com.apapedia.catalog.dto.CatalogListResponseDTO;
import com.apapedia.catalog.mapper.CatalogMapper;
import com.apapedia.catalog.model.Catalog;
import com.apapedia.catalog.model.Category;
import com.apapedia.catalog.service.CatalogService;
import com.apapedia.catalog.service.CategoryService;

import jakarta.transaction.Transactional;
import jakarta.validation.Valid;

import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.validation.BindingResult;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.HashMap;

@Transactional
@RestController
@RequestMapping("/catalog")
public class CatalogController {

    @Autowired
    private CatalogService catalogService;

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private CatalogMapper catalogMapper;


    @PostMapping("/create-catalog")
    public ResponseEntity<Map<String, String>> addCatalog(@Valid @ModelAttribute CreateCatalogRequestDTO catalogDTO, @RequestPart("file") MultipartFile file, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, "Request body has invalid type or missing field"
            );
        } else {
            Catalog catalog = catalogMapper.createCatalogRequestDTOToCatalog(catalogDTO);
            catalog.setCategory(catalogDTO.getCategory());
            catalog.setSellerId(UUID.fromString(catalogDTO.getSellerId()));

            // Process the file and set the image content in the Catalog entity
            byte[] imageContent;
            try {
                imageContent = catalogService.processFile(file);
            } catch (IOException e) {
                throw new ResponseStatusException(
                        HttpStatus.INTERNAL_SERVER_ERROR, "Error processing the file"
                );
            }
            catalog.setImage(imageContent);

            // Save the catalog entity
            catalogService.addCatalog(catalog);

            // Set the success message
            String successMessage = "Product '" + catalogDTO.getProductName() + "' created successfully.";

            // Return the success message as JSON
            Map<String, String> response = new HashMap<>();
            response.put("successMessage", successMessage);
            return ResponseEntity.ok(response);
        }
    }

    @GetMapping("/all-catalogs")
    public List<ShowCatalogRequestDTO> getAllCatalogsSortedByName() {
        List<Catalog> catalogs = catalogService.findAllSortedByName();
        return catalogs.stream()
                .map(catalogMapper::catalogToCatalogResponseDTO)
                .collect(Collectors.toList());
    }

    @GetMapping("/all-catalogs-by-seller-id")
    public List<ShowCatalogRequestDTO> getAllCatalogsBySellerId(@RequestParam UUID sellerId) {
        List<Catalog> catalogs = catalogService.findCatalogsBySellerId(sellerId);
        return catalogs.stream()
                .map(catalogMapper::catalogToCatalogResponseDTO)
                .collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ShowCatalogRequestDTO> getCatalogById(@PathVariable UUID id) {
        return catalogService.findCatalogById(id)
                .map(catalog -> ResponseEntity.ok(catalogMapper.catalogToCatalogResponseDTO(catalog)))
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<String> updateCatalog(
        @PathVariable UUID id,
        @ModelAttribute @Valid UpdateCatalogRequestDTO updateCatalogRequestDTO,
        @RequestParam(name = "file", required = false) MultipartFile file,
        BindingResult bindingResult) {

    if (bindingResult.hasErrors()) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body("Request body has invalid type or missing field");
    }

    // Convert categoryId to Long
    Long categoryId = Long.parseLong(updateCatalogRequestDTO.getCategoryId());

    // Populate category based on categoryId
    Category category = categoryService.getCategoryById(categoryId);
    if (category == null) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body("Invalid categoryId: " + updateCatalogRequestDTO.getCategoryId());
    }

    // Process the file and set the image content in the Catalog entity
    byte[] imageContent = null;
    if (file != null && !file.isEmpty()) {
        try {
            imageContent = catalogService.processFile(file);
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error processing the file");
        }
    }

    // Update the catalog entity
    catalogService.updateCatalog(id, updateCatalogRequestDTO, imageContent);

    // Return success message
    return ResponseEntity.ok("Product updated successfully");
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> softDeleteCatalog(@PathVariable UUID id) {
        catalogService.softDeleteCatalog(id);
        return ResponseEntity.ok("Catalog has been soft-deleted");
    }

    @GetMapping("/by-name")
    public ResponseEntity<List<CatalogListResponseDTO>> getCatalogsByProductName(@RequestParam String productName) {
        List<Catalog> catalogs = catalogService.findCatalogsByProductName(productName);
        List<CatalogListResponseDTO> responseDTOs = catalogs.stream()
                .map(catalogMapper::catalogToCatalogListResponseDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(responseDTOs);
    }

    @GetMapping("/by-price")
    public ResponseEntity<List<CatalogListResponseDTO>> getCatalogsByPriceRange(
            @RequestParam Integer minPrice,
            @RequestParam Integer maxPrice) {
        List<Catalog> catalogs = catalogService.findCatalogsByPriceRange(minPrice, maxPrice);
        List<CatalogListResponseDTO> responseDTOs = catalogs.stream()
                .map(catalogMapper::catalogToCatalogListResponseDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(responseDTOs);
    }

    @GetMapping("/sorted")
    public ResponseEntity<List<CatalogListResponseDTO>> getSortedCatalogs(
            @RequestParam String sortBy,  // Use this for the field by which you want to sort
            @RequestParam String sortOrder) {
        List<Catalog> catalogs = catalogService.findAllSorted(sortBy, sortOrder);
        List<CatalogListResponseDTO> responseDTOs = catalogs.stream()
                .map(catalogMapper::catalogToCatalogListResponseDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(responseDTOs);
    }
   
}
    
    
    
