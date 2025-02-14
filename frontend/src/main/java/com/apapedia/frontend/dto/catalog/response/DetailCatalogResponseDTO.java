
package com.apapedia.frontend.dto.catalog.response;
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
public class DetailCatalogResponseDTO {
    private UUID id;
    private String productName;
    private Integer price;
    private String productDescription;
    private Integer stock;
    private byte[] imageBase64;
    private ShowCategoryRequestDTO category;
        public void setImageBase64(String base64) {
        this.imageBase64 = Base64.getDecoder().decode(base64);
    }

    // Create a separate method to get the Base64-encoded string of the image
    public String getImageBase64() {
        return this.imageBase64 != null ? Base64.getEncoder().encodeToString(this.imageBase64) : null;
    }// o
}


