package com.apapedia.catalog.dto;
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
public class UpdateCatalogRequestDTO extends CreateCatalogRequestDTO{
    private UUID id;
    private String categoryId;
}