package com.apapedia.frontend.controller;

import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Map;
import java.util.Base64;
import java.util.UUID;

import com.apapedia.frontend.dto.catalog.response.UpdateCatalogResponseDTO;
import com.apapedia.frontend.dto.catalog.request.CreateCatalogRequestDTO;

@CrossOrigin(origins = "http://localhost:8083")
@Controller
@RequestMapping("/catalog")
public class CatalogController {
    
    String mainUrl = "http://localhost:8083";

    @GetMapping("/create")
    public String showAddCatalogForm(Model model) {
        model.addAttribute("createCatalogRequestDTO", new CreateCatalogRequestDTO());

        // Retrieve the list of categories using RestTemplate
        String categoriesUrl = mainUrl + "/category/all"; // Adjust the endpoint as per your backend
        ResponseEntity<List<Map<String, Object>>> categoriesResponse = new RestTemplate().exchange(
                categoriesUrl,
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<Map<String, Object>>>() {}
        );

        List<Map<String, Object>> categories = categoriesResponse.getBody();
        model.addAttribute("categories", categories);

        return "/catalog/addCatalog"; // Name of the HTML template (addCatalog.html)
    }

    @GetMapping("/all")
    public String showAllCatalogs(Model model) {
        // Use RestTemplate to get the list of catalogs from the REST service
        String catalogsUrl = mainUrl + "/catalog/allCatalogs";
        ResponseEntity<List<Map<String, Object>>> catalogsResponse = new RestTemplate().exchange(
                catalogsUrl,
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<Map<String, Object>>>() {}
        );
    
        List<Map<String, Object>> catalogs = catalogsResponse.getBody();
    
        // Convert byte array to Base64-encoded string in the model
        catalogs.forEach(catalog -> {
            // Explicitly convert the image field to a byte array
            byte[] imageBytes = catalog.get("image") instanceof String ?
                    Base64.getDecoder().decode((String) catalog.get("image")) :
                    (byte[]) catalog.get("image");
    
            catalog.put("imageBase64", Base64.getEncoder().encodeToString(imageBytes));
        });
    
        model.addAttribute("catalogs", catalogs);
    
        return "/catalog/allCatalogs"; // Name of the HTML template (allCatalogs.html)
    }

    @GetMapping("/update/{id}")
    public String showUpdateCatalogForm(@PathVariable UUID id, Model model) {
    // Use RestTemplate to get the catalog information
    String catalogUrl = mainUrl + "/catalog/" + id;
    ResponseEntity<UpdateCatalogResponseDTO> catalogResponse = new RestTemplate().exchange(
            catalogUrl,
            HttpMethod.GET,
            null,
            UpdateCatalogResponseDTO.class
    );
    UpdateCatalogResponseDTO catalog = catalogResponse.getBody();

    // Retrieve the list of categories
    String categoriesUrl = mainUrl + "/category/all"; // Adjust the endpoint as per your backend
    ResponseEntity<List<Map<String, Object>>> categoriesResponse = new RestTemplate().exchange(
            categoriesUrl,
            HttpMethod.GET,
            null,
            new ParameterizedTypeReference<List<Map<String, Object>>>() {}
    );
    List<Map<String, Object>> categories = categoriesResponse.getBody();

    model.addAttribute("updateCatalogResponseDTO", catalog);
    model.addAttribute("categories", categories);

    return "/catalog/updateCatalog"; // Name of the HTML template (updateCatalog.html)
    }

    @GetMapping("/search")
    public String searchCatalogs(@RequestParam String productName, Model model) {
        // Use RestTemplate to get the filtered list of catalogs from the REST service
        String searchUrl = mainUrl + "/catalog/by-name?productName=" + productName;
        ResponseEntity<List<Map<String, Object>>> searchResponse = new RestTemplate().exchange(
                searchUrl,
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<Map<String, Object>>>() {}
        );

        List<Map<String, Object>> searchResult = searchResponse.getBody();

        // Convert byte array to Base64-encoded string in the model
        searchResult.forEach(catalog -> {
            // Explicitly convert the image field to a byte array
            byte[] imageBytes = catalog.get("image") instanceof String ?
                    Base64.getDecoder().decode((String) catalog.get("image")) :
                    (byte[]) catalog.get("image");

            catalog.put("imageBase64", Base64.getEncoder().encodeToString(imageBytes));
        });

        model.addAttribute("catalogs", searchResult);

        return "/catalog/searchResult"; // Name of the HTML template (searchResult.html)
    }

    @GetMapping("/filterByPrice")
    public String filterCatalogsByPrice(
            @RequestParam Integer minPrice,
            @RequestParam Integer maxPrice,
            Model model) {
        // Use RestTemplate to get the filtered list of catalogs from the REST service
        String filterUrl = mainUrl + "/catalog/by-price?minPrice=" + minPrice + "&maxPrice=" + maxPrice;
        ResponseEntity<List<Map<String, Object>>> filterResponse = new RestTemplate().exchange(
                filterUrl,
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<Map<String, Object>>>() {}
        );

        List<Map<String, Object>> filterResult = filterResponse.getBody();

        // Convert byte array to Base64-encoded string in the model
        filterResult.forEach(catalog -> {
            // Explicitly convert the image field to a byte array
            byte[] imageBytes = catalog.get("image") instanceof String ?
                    Base64.getDecoder().decode((String) catalog.get("image")) :
                    (byte[]) catalog.get("image");

            catalog.put("imageBase64", Base64.getEncoder().encodeToString(imageBytes));
        });

        model.addAttribute("catalogs", filterResult);

        return "/catalog/filterResult"; // Name of the HTML template (filterResult.html)
    }

    @GetMapping("/sort")
    public String sortCatalogs(
            @RequestParam String sortBy,
            @RequestParam String sortOrder,
            Model model) {
        // Use RestTemplate to get the sorted list of catalogs from the REST service
        String sortUrl = mainUrl + "/catalog/sorted?sortBy=" + sortBy + "&sortOrder=" + sortOrder;
        ResponseEntity<List<Map<String, Object>>> sortResponse = new RestTemplate().exchange(
                sortUrl,
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<Map<String, Object>>>() {}
        );

        List<Map<String, Object>> sortResult = sortResponse.getBody();

        // Convert byte array to Base64-encoded string in the model
        sortResult.forEach(catalog -> {
            // Explicitly convert the image field to a byte array
            byte[] imageBytes = catalog.get("image") instanceof String ?
                    Base64.getDecoder().decode((String) catalog.get("image")) :
                    (byte[]) catalog.get("image");

            catalog.put("imageBase64", Base64.getEncoder().encodeToString(imageBytes));
        });

        model.addAttribute("catalogs", sortResult);

        return "/catalog/sortResult"; // Name of the HTML template (sortResult.html)
    }
}
