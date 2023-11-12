package com.apapedia.catalog;

import com.apapedia.catalog.DTO.CreateCatalogRequestDTO;
import com.apapedia.catalog.Mapper.CatalogMapper;
import com.apapedia.catalog.model.Category;
import com.apapedia.catalog.service.CatalogService;
import com.apapedia.catalog.service.CategoryService;
import com.github.javafaker.Faker;

import jakarta.transaction.Transactional;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;



import org.springframework.web.multipart.MultipartFile;


import java.util.*; 
// No changes needed, the import statement is already correct
import java.util.UUID;


@SpringBootApplication(exclude = {SecurityAutoConfiguration.class})
public class CatalogApplication {
	public static void main(String[] args) {
		SpringApplication.run(CatalogApplication.class, args);
	}
	
@Bean
@Transactional
CommandLineRunner run(CatalogService catalogService, CatalogMapper catalogMapper, CategoryService categoryService) {
    return args -> {
        Faker faker = new Faker();

        // Create fake data for Category
        Category category = new Category();
        category.setName(faker.commerce().department());
        // Save the Category entity and ensure it's persisted before using its ID
        category = categoryService.addCategory(category);

        // Create fake data for Catalog
        CreateCatalogRequestDTO catalogDTO = new CreateCatalogRequestDTO();
        catalogDTO.setProductName(faker.commerce().productName());
        catalogDTO.setPrice(faker.number().numberBetween(100, 1000));
        catalogDTO.setProductDescription(faker.commerce().material());
        catalogDTO.setStock(faker.number().numberBetween(1, 100));
        catalogDTO.setCategory(category); // Set the persisted Category

        // Create a byte array to simulate file content
        byte[] imageContent = new byte[20]; // Example image content
        new java.util.Random().nextBytes(imageContent);
        catalogDTO.setImage(imageContent);

        // Map DTO to Entity
        var catalog = catalogMapper.createCatalogRequestDTOToCatalog(catalogDTO);
        catalog.setImage(imageContent); // Directly set the byte array

        // Save the Catalog entity
        catalogService.addCatalog(catalog);
    };
}


}
