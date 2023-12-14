package com.apapedia.catalog;
import com.apapedia.catalog.dto.CreateCatalogRequestDTO;
import com.apapedia.catalog.mapper.CatalogMapper;
import com.apapedia.catalog.model.Category;
import com.apapedia.catalog.service.CatalogService;
import com.apapedia.catalog.service.CategoryService;
import com.github.javafaker.Faker;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import java.util.*;


@SpringBootApplication(exclude = {SecurityAutoConfiguration.class})
public class CatalogApplication {
    public static void main(String[] args) {
        SpringApplication.run(CatalogApplication.class, args);
    }

    @Bean
    CommandLineRunner initializeDatabase(CategoryService categoryService, CatalogService catalogService, CatalogMapper catalogMapper) {
        return args -> {
            // Preload categories if they are not already present
            List<String> predefinedCategoryNames = Arrays.asList(
                "Aksesoris Fashion",
                "Buku & Alat Tulis",
                "Elektronik",
                "Fashion Bayi & Anak",
                "Fashion Muslim",
                "Fotografi",
                "Hobi & Koleksi",
                "Jam Tangan",
                "Perawatan & Kecantikan",
                "Makanan & Minuman",
                "Otomotif",
                "Perlengkapan Rumah",
                "Souvenir & Party Supplies"
                // ... other predefined category names ...
            );
            // Check if categories need to be preloaded
            if (categoryService.getAllCategories().isEmpty()) {
                predefinedCategoryNames.forEach(categoryName -> {
                    var category = new Category();
                    category.setName(categoryName);
                    categoryService.addCategory(category);
                });
            }
            // Now that categories are ensured to be preloaded, continue with creating fake catalog entries
            var faker = new Faker();
            var category = categoryService.getAllCategories().get(faker.number().numberBetween(1, 13)); // example: get the first category
            // Create fake data for Catalog
            var catalogDTO = new CreateCatalogRequestDTO();
            catalogDTO.setProductName(faker.commerce().productName());
            catalogDTO.setPrice(faker.number().numberBetween(100, 1000));
            catalogDTO.setProductDescription(faker.commerce().material());
            catalogDTO.setStock(faker.number().numberBetween(1, 100));
            catalogDTO.setCategory(category); // Set the fetched Category
            // Create a byte array to simulate file content
            var imageContent = new byte[20]; // Example image content
            new Random().nextBytes(imageContent);
            catalogDTO.setImage(imageContent);
            // Map DTO to Entity
            var catalog = catalogMapper.createCatalogRequestDTOToCatalog(catalogDTO);
            catalog.setImage(imageContent); // Directly set the byte array
            // Save the Catalog entity
            catalogService.addCatalog(catalog);
        };
    }
}