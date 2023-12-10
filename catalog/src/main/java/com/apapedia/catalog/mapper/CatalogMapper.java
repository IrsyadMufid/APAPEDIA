package com.apapedia.catalog.mapper;

import com.apapedia.catalog.dto.CreateCatalogRequestDTO;
import com.apapedia.catalog.dto.ShowCatalogRequestDTO;
import com.apapedia.catalog.dto.UpdateCatalogRequestDTO;
import com.apapedia.catalog.dto.CatalogListResponseDTO;
import com.apapedia.catalog.model.Catalog;
import java.io.IOException;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.web.multipart.MultipartFile;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring", uses = CategoryMapper.class)
public interface CatalogMapper {


    Catalog createCatalogRequestDTOToCatalog(CreateCatalogRequestDTO CreateCatalogRequestDTO);

    @Mapping(target = "category.id", source = "categoryId")
    Catalog updateCatalogRequestDTOToCatalog(UpdateCatalogRequestDTO updateCatalogRequestDTO, @MappingTarget Catalog catalog);


    ShowCatalogRequestDTO catalogToCatalogResponseDTO(Catalog catalog);

    CatalogListResponseDTO catalogToCatalogListResponseDTO(Catalog catalog);

    default byte[] map(MultipartFile file) {
        try {
            return file.getBytes();
        } catch (IOException e) {
            throw new RuntimeException("Error reading file content", e);
        }
    }

}
