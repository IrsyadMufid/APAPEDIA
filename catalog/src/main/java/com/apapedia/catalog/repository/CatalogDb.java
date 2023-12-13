package com.apapedia.catalog.repository;



import com.apapedia.catalog.model.Catalog;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.UUID;
import java.util.List;

public interface CatalogDb extends JpaRepository<Catalog, UUID> {
    List<Catalog> findAllByOrderByProductNameAsc();
    List<Catalog> findByProductNameContainingIgnoreCase(String productName);
    List<Catalog> findByPriceBetween(Integer minPrice, Integer maxPrice);
    List<Catalog> findBySellerId(UUID sellerId);
}
