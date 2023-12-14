package com.apapedia.catalog.dto;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.Base64;
@AllArgsConstructor
@NoArgsConstructor
@Data
public class ShowCatalogRequestDTO {
    private UUID id;
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
    public String getImageBase64() {
        return Base64.getEncoder().encodeToString(this.image);
    }
}