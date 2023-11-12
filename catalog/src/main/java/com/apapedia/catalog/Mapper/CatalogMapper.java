package com.apapedia.catalog.Mapper;



import com.apapedia.catalog.DTO.CreateCatalogRequestDTO;
import com.apapedia.catalog.model.Catalog;

import java.io.IOException;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;
import org.springframework.web.multipart.MultipartFile;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface CatalogMapper {


    Catalog createCatalogRequestDTOToCatalog(CreateCatalogRequestDTO CreateCatalogRequestDTO);

    default byte[] map(MultipartFile file) {
        try {
            return file.getBytes();
        } catch (IOException e) {
            throw new RuntimeException("Error reading file content", e);
        }
    }

}
