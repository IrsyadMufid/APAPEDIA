package com.apapedia.catalog.service;



import com.apapedia.catalog.model.Catalog;
import com.apapedia.catalog.repository.CatalogDb;

import jakarta.transaction.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Transactional
public class CatalogServiceImpl implements CatalogService {

    @Autowired
    private CatalogDb catalogDb;

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
}
