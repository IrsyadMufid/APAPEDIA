package com.apapedia.order.dto;

import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class CatalogResponse {
    private UUID id;
    private UUID sellerId;
    private Integer price;
    private String productName;
    private String productDescription;
    private Integer stock;
}
