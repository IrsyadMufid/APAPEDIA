package com.apapedia.frontend.dto.catalog.request;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

@Data
public class CatalogRequestDTO {
    private String productName;
    private Integer price;
    private String productDescription;
    private Integer stock;
    private MultipartFile file;
}
