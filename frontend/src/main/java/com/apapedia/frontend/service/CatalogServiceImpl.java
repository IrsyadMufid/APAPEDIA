package com.apapedia.frontend.service;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Service;
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

import com.apapedia.frontend.dto.catalog.request.CreateCatalogFormModel;
import com.apapedia.frontend.dto.catalog.response.CatalogListResponseDTO;
import com.apapedia.frontend.dto.catalog.response.CatalogResponseDTO;
import com.apapedia.frontend.dto.catalog.response.DetailCatalogResponseDTO;
import com.apapedia.frontend.dto.catalog.response.UpdateCatalogResponseDTO;
import com.apapedia.frontend.setting.Setting;

@Service
public class CatalogServiceImpl implements CatalogService {

@Autowired
private RestTemplate restTemplate;


    
    private final String baseUrl = Setting.CLIENT_CATALOG_SERVICE + "/catalog";
    private final String categoriesUrl = Setting.CLIENT_CATALOG_SERVICE + "/category/all";
    private final String allCatalogsUrl = baseUrl + "/all-catalogs";
    private final String catalogsBySellerUrl = baseUrl + "/all-catalogs-by-seller-id?sellerId=";
    private final String searchUrl = baseUrl + "/by-name?productName=";
    private final String filterUrl = baseUrl + "/by-price?minPrice={minPrice}&maxPrice={maxPrice}";
    private final String sortUrl = baseUrl + "/sorted?sortBy={sortBy}&sortOrder={sortOrder}";


    @Override
    public List<Map<String, Object>> getAllCategories() {
        ResponseEntity<List<Map<String, Object>>> categoriesResponse = restTemplate.exchange(
                categoriesUrl,
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<Map<String, Object>>>() {}
        );
        return categoriesResponse.getBody();
    }

    @Override
    public void prepareAddCatalogForm(Model model, UUID activeUserId) {
        List<Map<String, Object>> categories = getAllCategories();
        model.addAttribute("categories", categories);

        CreateCatalogFormModel createCatalogFormModel = new CreateCatalogFormModel();
        createCatalogFormModel.setSellerId(activeUserId);
        String catalogServiceUrl = Setting.CLIENT_CATALOG_SERVICE;
        String createCatalogUrl = catalogServiceUrl + "/catalog/create-catalog";
        model.addAttribute("createCatalogUrl", createCatalogUrl);
        model.addAttribute("createCatalogFormModel", createCatalogFormModel);
        model.addAttribute("activeUserId", activeUserId);
    }

    @Override
    public List<Map<String, Object>> getAllCatalogs(UUID activeUserId) {
        ResponseEntity<List<Map<String, Object>>> catalogsResponse = restTemplate.exchange(
                allCatalogsUrl,
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<Map<String, Object>>>() {}
        );

        List<Map<String, Object>> catalogs = catalogsResponse.getBody();

        // Convert byte array to Base64-encoded string in the model
        catalogs.forEach(catalog -> {
            byte[] imageBytes = catalog.get("image") instanceof String ?
                    Base64.getDecoder().decode((String) catalog.get("image")) :
                    (byte[]) catalog.get("image");

            catalog.put("imageBase64", Base64.getEncoder().encodeToString(imageBytes));
        });

        return catalogs;
    }

    @Override
    public List<Map<String, Object>> getAllCatalogsBySeller(UUID sellerId) {
        String urlWithSellerId = catalogsBySellerUrl + sellerId;
        ResponseEntity<List<Map<String, Object>>> catalogsResponse = restTemplate.exchange(
                urlWithSellerId,
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<Map<String, Object>>>() {}
        );

        List<Map<String, Object>> catalogs = catalogsResponse.getBody();

        // Convert byte array to Base64-encoded string in the model
        catalogs.forEach(catalog -> {
            byte[] imageBytes = catalog.get("image") instanceof String ?
                    Base64.getDecoder().decode((String) catalog.get("image")) :
                    (byte[]) catalog.get("image");

            catalog.put("imageBase64", Base64.getEncoder().encodeToString(imageBytes));
        });

        return catalogs;
    }


    @Override
    public UpdateCatalogResponseDTO getCatalogById(UUID id) {
        String url = baseUrl +"/"+id;
        ResponseEntity<UpdateCatalogResponseDTO> catalogResponse = restTemplate.exchange(
                url,
                HttpMethod.GET,
                null,
                UpdateCatalogResponseDTO.class
        );
        return catalogResponse.getBody();
    }

    @Override
    public void prepareUpdateCatalogForm(UUID id, Model model, UUID activeUserId) {
        UpdateCatalogResponseDTO catalog = getCatalogById(id);
        catalog.setSellerId(activeUserId);
        List<Map<String, Object>> categories = getAllCategories();

        model.addAttribute("baseUrl", baseUrl);
        model.addAttribute("updateCatalogResponseDTO", catalog);
        model.addAttribute("categories", categories);
    }

    @Override
    public List<Map<String, Object>> searchCatalogsByName(String productName) {
        String url = searchUrl + productName;
        ResponseEntity<List<Map<String, Object>>> searchResponse = restTemplate.exchange(
                url,
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<Map<String, Object>>>() {}
        );

        List<Map<String, Object>> searchResult = searchResponse.getBody();

        // Convert byte array to Base64-encoded string in the model
        searchResult.forEach(catalog -> {
            byte[] imageBytes = catalog.get("image") instanceof String ?
                    Base64.getDecoder().decode((String) catalog.get("image")) :
                    (byte[]) catalog.get("image");

            catalog.put("imageBase64", Base64.getEncoder().encodeToString(imageBytes));
        });

        return searchResult;
    }

    @Override
    public DetailCatalogResponseDTO getCatalogDetailById(UUID id) {
        String url = baseUrl +'/' +id;
        ResponseEntity<DetailCatalogResponseDTO> catalogResponse = restTemplate.exchange(
                url,
                HttpMethod.GET,
                null,
                DetailCatalogResponseDTO.class
        );
        return catalogResponse.getBody();
    }

    @Override
    public List<Map<String, Object>> filterCatalogsByPrice(Integer minPrice, Integer maxPrice) {
        ResponseEntity<List<Map<String, Object>>> filterResponse = restTemplate.exchange(
                filterUrl,
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<Map<String, Object>>>() {},
                minPrice,
                maxPrice
        );

        List<Map<String, Object>> filterResult = filterResponse.getBody();

        // Convert byte array to Base64-encoded string in the model
        filterResult.forEach(catalog -> {
            byte[] imageBytes = catalog.get("image") instanceof String ?
                    Base64.getDecoder().decode((String) catalog.get("image")) :
                    (byte[]) catalog.get("image");

            catalog.put("imageBase64", Base64.getEncoder().encodeToString(imageBytes));
        });

        return filterResult;
    }

    @Override
    public List<Map<String, Object>> sortCatalogs(String sortBy, String sortOrder) {
        ResponseEntity<List<Map<String, Object>>> sortResponse = restTemplate.exchange(
                sortUrl,
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<Map<String, Object>>>() {},
                sortBy,
                sortOrder
        );

        List<Map<String, Object>> sortResult = sortResponse.getBody();

        // Convert byte array to Base64-encoded string in the model
        sortResult.forEach(catalog -> {
            byte[] imageBytes = catalog.get("image") instanceof String ?
                    Base64.getDecoder().decode((String) catalog.get("image")) :
                    (byte[]) catalog.get("image");

            catalog.put("imageBase64", Base64.getEncoder().encodeToString(imageBytes));
        });

        return sortResult;
    }

}

