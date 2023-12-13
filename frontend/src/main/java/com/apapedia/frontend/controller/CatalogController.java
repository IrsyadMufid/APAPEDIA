package com.apapedia.frontend.controller;

import org.springframework.beans.factory.annotation.Autowired;
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
import java.util.HashMap;
import java.util.UUID;

import com.apapedia.frontend.dto.catalog.response.DetailCatalogResponseDTO;
import com.apapedia.frontend.dto.order.ChartDataDTO;
import com.apapedia.frontend.service.CatalogService;
import com.apapedia.frontend.setting.Setting;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

@CrossOrigin
@Controller
@RequestMapping("/catalog")
public class CatalogController {
    
    @Autowired
    private CatalogService catalogService;

    @GetMapping("/create")
    public String showAddCatalogForm(Model model, HttpServletRequest request) {
        var activeUserId = (UUID) request.getSession().getAttribute("activeUserId");
        catalogService.prepareAddCatalogForm(model, activeUserId);
        return "/catalog/addCatalog"; // Name of the HTML template (addCatalog.html)
    }

    @GetMapping("/all")
    public String showAllCatalogs(Model model, HttpServletRequest request) {
        HttpSession session = request.getSession();
        UUID activeUserId = (UUID) session.getAttribute("activeUserId");

        List<Map<String, Object>> catalogs = catalogService.getAllCatalogs(activeUserId);

        model.addAttribute("activeUserId", activeUserId);
        model.addAttribute("catalogs", catalogs);

        return "/catalog/allCatalogs"; // Name of the HTML template (allCatalogs.html)
    }
    
    @GetMapping("/all-seller/{id}")
    public String showAllCatalogsSeller(Model model, HttpServletRequest request) {
        HttpSession session = request.getSession();
        UUID sellerId = (UUID) session.getAttribute("activeUserId");

        List<Map<String, Object>> catalogs = catalogService.getAllCatalogsBySeller(sellerId);
        RestTemplate restTemplate = new RestTemplate();

        // Define the URL with path variables
        String url = Setting.CLIENT_ORDER_SERVICE + "/order/selling-data/" + sellerId.toString();

        // Set the path variable values
        Map<String, String> uriVariables = new HashMap<>();
        uriVariables.put("sellerId", sellerId.toString());

        // Make the GET request and retrieve the response
        ResponseEntity<List<ChartDataDTO>> responseEntity = restTemplate.exchange(
            url,
            HttpMethod.GET,
            null,
            new ParameterizedTypeReference<List<ChartDataDTO>>() {},
            uriVariables
        );

        // Extract the response body
        List<ChartDataDTO> listResponse = responseEntity.getBody();

        // Add the list to the model
        model.addAttribute("list", listResponse);
        model.addAttribute("activeUserId", sellerId);
        model.addAttribute("catalogs", catalogs);

        return "/catalog/allCatalogsSeller"; // Name of the HTML template (allCatalogsSeller.html)
    }

    @GetMapping("/update/{id}")
    public String showUpdateCatalogForm(@PathVariable UUID id, Model model, HttpServletRequest request) {
        var activeUserId = (UUID) request.getSession().getAttribute("activeUserId");
        catalogService.prepareUpdateCatalogForm(id, model, activeUserId);

        return "/catalog/updateCatalog"; // Name of the HTML template (updateCatalog.html)
    }

   @GetMapping("/search")
    public String searchCatalogs(@RequestParam String productName, Model model) {
        List<Map<String, Object>> searchResult = catalogService.searchCatalogsByName(productName);
        model.addAttribute("catalogs", searchResult);

        return "/catalog/searchResult"; // Name of the HTML template (searchResult.html)
    }

    @GetMapping("/detail/{id}")
    public String detailCatalog(@PathVariable UUID id, Model model) {
        DetailCatalogResponseDTO catalog = catalogService.getCatalogDetailById(id);
        model.addAttribute("catalog", catalog);

        return "/catalog/detailCatalog"; // Name of the HTML template (detailCatalog.html)
    }

    @GetMapping("/filterByPrice")
    public String filterCatalogsByPrice(
            @RequestParam Integer minPrice,
            @RequestParam Integer maxPrice,
            Model model) {
        List<Map<String, Object>> filterResult = catalogService.filterCatalogsByPrice(minPrice, maxPrice);
        model.addAttribute("catalogs", filterResult);

        return "/catalog/filterResult"; // Name of the HTML template (filterResult.html)
    }

    @GetMapping("/sort")
    public String sortCatalogs(
            @RequestParam String sortBy,
            @RequestParam String sortOrder,
            Model model) {
        List<Map<String, Object>> sortResult = catalogService.sortCatalogs(sortBy, sortOrder);
        model.addAttribute("catalogs", sortResult);

        return "/catalog/sortResult"; // Name of the HTML template (sortResult.html)
    }



}
