package com.apapedia.frontend.dto.catalog.request;

import jakarta.validation.constraints.NotNull;
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
public class CreateCatalogRequestDTO {
    
    @NotNull(message = "Product Name cannot be null")
    private String productName;

    @NotNull(message = "Product Price cannot be null")
    private Integer price;

    private String productDescription;

    @NotNull(message = "Product Stock cannot be null")
    private Integer stock;

    private byte[] image;

    private Category category;
}
