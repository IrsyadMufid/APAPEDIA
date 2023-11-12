package com.apapedia.catalog.repository;

import com.apapedia.catalog.model.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;
import java.util.UUID;

public interface CategoryDb extends JpaRepository<Category, UUID> {
    Optional<Category> findByName(String name);
}
