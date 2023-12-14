package com.apapedia.catalog;

import com.apapedia.catalog.dto.UpdateCatalogRequestDTO;
import com.apapedia.catalog.model.Catalog;
import com.apapedia.catalog.service.CatalogService;
import com.apapedia.catalog.service.CategoryService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;


@SpringBootTest
class CatalogApplicationTests {

    @Autowired
    private CatalogService catalogService;

@Test
    @Transactional
    void testCreateCatalog() throws IOException {
        // Create a mock MultipartFile
        MockMultipartFile file = new MockMultipartFile("file", "test.jpg", "image/jpeg", "test data".getBytes());

        // Create a Catalog object for testing
        Catalog catalog = new Catalog();
        catalog.setProductName("Test Product");
        catalog.setPrice(100);
        catalog.setStock(50);

        // Process the file and set the image content in the Catalog entity
        catalog.setImage(catalogService.processFile(file));

        // Call the addCatalog method
        Catalog savedCatalog = catalogService.addCatalog(catalog);

        // Assertions
        assertNotNull(savedCatalog.getId());
        assertEquals("Test Product", savedCatalog.getProductName());
        assertEquals(100, savedCatalog.getPrice());
        assertEquals(50, savedCatalog.getStock());
        assertFalse(savedCatalog.getIsDeleted());
        assertNotNull(savedCatalog.getImage());
        // Add more assertions based on your requirements
    }

	@Test
	@Transactional
	void testUpdateCatalog() throws IOException {
		// Create a mock MultipartFile
		MockMultipartFile file = new MockMultipartFile("file", "test.jpg", "image/jpeg", "test data".getBytes());
	
		// Create a Catalog object and save it for testing
		Catalog catalog = new Catalog();
		catalog.setProductName("Test Product");
		catalog.setPrice(100);
		catalog.setStock(50);
		Catalog savedCatalog = catalogService.addCatalog(catalog);
	
		// Create an UpdateCatalogRequestDTO for testing
		UpdateCatalogRequestDTO updateDTO = new UpdateCatalogRequestDTO();
		updateDTO.setCategoryId("1"); // Assuming category ID is 1 for testing
		updateDTO.setProductName("Updated Product Name");
		updateDTO.setPrice(150);
		updateDTO.setStock(75); // Set the updated stock
	
		// Process the file and set the image content in the Catalog entity
		byte[] imageContent = catalogService.processFile(file);
		updateDTO.setImage(imageContent);
	
		// Call the updateCatalog method
		Catalog updatedCatalog = catalogService.updateCatalog(savedCatalog.getId(), updateDTO, imageContent);
	
		// Assertions
		assertNotNull(updatedCatalog);
		assertEquals("Updated Product Name", updatedCatalog.getProductName());
		assertEquals(150, updatedCatalog.getPrice());
		assertEquals(75, updatedCatalog.getStock()); // Fix the expected stock
		assertFalse(updatedCatalog.getIsDeleted());
		assertNotNull(updatedCatalog.getImage());
	}
	
	

    @Test
    @Transactional
    void testFindCatalogById() {
        Catalog catalog = new Catalog();
        catalog.setProductName("Test Product");
        catalog.setPrice(100);
        catalog.setStock(50);

        Catalog savedCatalog = catalogService.addCatalog(catalog);

        Catalog foundCatalog = catalogService.findCatalogById(savedCatalog.getId()).orElse(null);

        assertNotNull(foundCatalog);
        assertEquals(savedCatalog.getId(), foundCatalog.getId());
        assertEquals("Test Product", foundCatalog.getProductName());
        // Add more assertions based on your requirements
    }

	@Test
	@Transactional
	void testSortCatalogs() {
		// Create at least two catalogs for sorting comparison
		Catalog catalog1 = createCatalog("Product B", 150);
		Catalog catalog2 = createCatalog("Product A", 100);
	
		// Save the catalogs to the database
		catalogService.addCatalog(catalog1);
		catalogService.addCatalog(catalog2);
	
		// Fetch the sorted catalogs
		List<Catalog> sortedCatalogs = catalogService.findAllSorted("productName", "asc");
	
		// Assertions
		assertTrue(sortedCatalogs.size() >= 2);
		assertTrue(sortedCatalogs.get(0).getProductName().compareTo(sortedCatalogs.get(1).getProductName()) <= 0);
		// Add more assertions based on your requirements for sorted catalogs
	}
	
	// Helper method to create a catalog
	private Catalog createCatalog(String productName, int price) {
		Catalog catalog = new Catalog();
		catalog.setProductName(productName);
		catalog.setPrice(price);
		catalog.setStock(50);
		return catalog;
	}

    // Add more test cases as needed for other operations

}

