package com.apapedia.frontend.service;

import java.util.List;

import com.apapedia.frontend.dto.catalog.request.CreateCatalogRequestDTO;
import com.apapedia.frontend.dto.catalog.response.CatalogListResponseDTO;

public interface CatalogService {
    CatalogListResponseDTO addCatalog(String token, CreateCatalogRequestDTO catalogDTO);
    
    List<CatalogListResponseDTO> getCatalogList(String token);

    CatalogListResponseDTO getCatalogById(String id, String token);

    


}
