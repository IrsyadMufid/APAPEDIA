package com.apapedia.catalog.dto;

import java.util.Base64;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class CatalogListResponseDTO {
    private String id;
    private Integer price;
    private String productName;
    private String productDescription;
    private Integer stock;
    private byte[] image;
    private Boolean isDeleted = false;
    private ShowCategoryRequestDTO category;
    
    public void setImageBase64(String base64) {
        this.image = Base64.getDecoder().decode(base64);
    }

    // Create a separate method to get the Base64-encoded string of the image
    public String getImageBase64() {
        return this.image != null ? Base64.getEncoder().encodeToString(this.image) : null;
    }// o// or CategoryDTO if needed
}
