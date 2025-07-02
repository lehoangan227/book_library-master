package com.project.Book.mapper;

import com.project.Book.dto.request.CategoryRequest;
import com.project.Book.dto.request.CategoryUpdateRequest;
import com.project.Book.dto.response.CategoryResponse;
import com.project.Book.entity.Category;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface CategoryMapper {
    Category requestDtoToEntity(CategoryRequest categoryRequest);
    CategoryResponse entityToResponseDTO(Category category);
    void updateDtoToEntity(@MappingTarget Category category, CategoryUpdateRequest categoryUpdateRequest);
}
