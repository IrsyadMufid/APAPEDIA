package com.apapedia.catalog.repository;

import com.apapedia.catalog.model.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;


public interface CategoryDb extends JpaRepository<Category, Long> {
    Optional<Category> findByName(String name);
}
