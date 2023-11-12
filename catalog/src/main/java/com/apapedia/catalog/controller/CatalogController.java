package com.apapedia.catalog.controller;

import com.apapedia.catalog.DTO.CreateCatalogRequestDTO;
import com.apapedia.catalog.Mapper.CatalogMapper;
import com.apapedia.catalog.model.Catalog;
import com.apapedia.catalog.service.CatalogService;

import jakarta.transaction.Transactional;
import jakarta.validation.Valid;

import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.validation.BindingResult;
import java.util.List;

@Transactional
@RestController
@RequestMapping("/catalog")
public class CatalogController {

    @Autowired
    private CatalogService catalogService;

    @Autowired
    private CatalogMapper catalogMapper;

 @PostMapping("/create")
public Catalog addCatalog(@Valid @RequestBody CreateCatalogRequestDTO catalogDTO, BindingResult bindingResult) {
    if (bindingResult.hasErrors()) {
        throw new ResponseStatusException(
                HttpStatus.BAD_REQUEST, "Request body has invalid type or missing field"
        );
    } else {
        // Assuming you have a MapStruct mapper instance called 'catalogMapper'
        Catalog catalog = catalogMapper.createCatalogRequestDTOToCatalog(catalogDTO);
        // Handle the image separately if it's not null and not empty
        catalogService.addCatalog(catalog);
        return catalog;
    }
}

@GetMapping("/all")
public List<Catalog> getAllCatalogsSortedByName() {
    return catalogService.findAllSortedByName();
}

@GetMapping("/{id}")
    public ResponseEntity<Catalog> getCatalogById(@PathVariable UUID id) {
        return catalogService.findCatalogById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

}
