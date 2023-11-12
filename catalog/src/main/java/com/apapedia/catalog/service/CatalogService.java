package com.apapedia.catalog.service;


import com.apapedia.catalog.model.Catalog;

import jakarta.transaction.Transactional;

import java.util.List;

import java.util.UUID;
import java.util.Optional;

public interface CatalogService {
    Catalog addCatalog(Catalog catalog);
    List<Catalog> findAllSortedByName();
    public Optional<Catalog> findCatalogById(UUID id);

}
