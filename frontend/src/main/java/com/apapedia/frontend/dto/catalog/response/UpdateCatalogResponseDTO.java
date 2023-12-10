package com.apapedia.frontend.dto.catalog.response;

import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Getter
@Setter
public class UpdateCatalogResponseDTO {
    private UUID id;
    private String productName;
    private Integer price;
    private String productDescription;
    private Integer stock;
    private String imageBase64;
    private ShowCategoryRequestDTO category;
}