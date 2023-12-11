package com.apapedia.catalog.dto;

import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.util.Base64;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Getter
@Setter
public class ShowCatalogRequestDTO {
    private UUID id;
    private Integer price;
    private String productName;
    private String productDescription;
    private Integer stock;
    private byte[] image;
    private Boolean isDeleted = false;
    private ShowCategoryRequestDTO category;
    private UUID sellerId;

    // Other fields, getters, and setters...

    // Create a separate method to set the image as Base64
    public void setImageBase64(String base64) {
        this.image = Base64.getDecoder().decode(base64);
    }

    // Create a separate method to get the Base64-encoded string of the image
    public String getImageBase64() {
        return Base64.getEncoder().encodeToString(this.image);
    }
}
