package com.apapedia.frontend.dto.catalog.response;

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
public class CatalogListResponseDTO {
    private String id;
    private Integer price;
    private String productName;
    private String productDescription;
    private Integer stock;
    private byte[] image;
    private Boolean isDeleted = false;
    private ShowCategoryRequestDTO category;    // or CategoryDTO if needed
}
