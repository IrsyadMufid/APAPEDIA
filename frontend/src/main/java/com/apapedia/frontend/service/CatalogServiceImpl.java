package com.apapedia.frontend.service;

import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.List;

import com.apapedia.frontend.dto.catalog.response.CatalogListResponseDTO;
import com.apapedia.frontend.dto.catalog.response.CatalogResponseDTO;

@Service
public class CatalogServiceImpl implements CatalogService {
    private final WebClient webClient;

    public CatalogServiceImpl(WebClient.Builder webClientBuilder) {
        this.webClient = webClientBuilder.baseUrl("http://localhost:8083")
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .build();
    }

    // @Override
    // public CatalogResponseDTO addCatalog(String token, CreateCatalogRequestDTO createCatalogRequestDTO) {
    //     var response = this.webClient
    //             .post()
    //             .uri("/api/catalog/add-catalog")
    //             .header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
    //             .bodyValue(createCatalogRequestDTO)
    //             .retrieve()
    //             .bodyToMono(CatalogResponseDTO.class)
    //             .block();
        
    //     return response;
    // }

    // @Override
    // public List<CatalogListResponseDTO> getCatalogList(String token) {
    //     var response = this.webClient
    //             .get()
    //             .uri("/api/catalog/all-catalogs")
    //             .header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
    //             .retrieve()
    //             .bodyToMono(CatalogResponseDTO.class)
    //             .block();
        
    //     return response.getCatalogList();
    // }


    
}
