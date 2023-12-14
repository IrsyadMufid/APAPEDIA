package com.apapedia.catalog.service;

import com.apapedia.catalog.dto.UpdateCatalogRequestDTO;
import com.apapedia.catalog.model.Catalog;

import jakarta.transaction.Transactional;

import java.io.IOException;
import java.util.List;

import java.util.UUID;

import org.springframework.web.multipart.MultipartFile;

import java.util.Optional;

@Transactional
public interface CatalogService {
    Catalog addCatalog(Catalog catalog);
    public List<Catalog> findAll();
    List<Catalog> findAllSortedByName();
    public Optional<Catalog> findCatalogById(UUID id);
    Catalog updateCatalog(UUID id, UpdateCatalogRequestDTO updateCatalogRequestDTO, byte[] imageContent);
    void softDeleteCatalog(UUID id);
    List<Catalog> findCatalogsByProductName(String productName);
    List<Catalog> findCatalogsBySellerId(UUID sellerId);
    List<Catalog> findCatalogsByPriceRange(Integer minPrice, Integer maxPrice);
    List<Catalog> findAllSorted(String sortBy, String sortOrder);
    public byte[] processFile(MultipartFile file) throws IOException;
}
