package com.apapedia.catalog.mapper;
import com.apapedia.catalog.dto.ShowCategoryRequestDTO;
import com.apapedia.catalog.model.Category;
import org.mapstruct.Mapper;
@Mapper(componentModel = "spring")
public interface CategoryMapper {
    ShowCategoryRequestDTO categoryToCategoryResponseDTO(Category category);
    Category map(Long categoryId);
}
