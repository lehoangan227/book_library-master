package com.project.Book.mapper;

import com.project.Book.dto.response.LikeResponse;
import com.project.Book.entity.LikePost;
import org.mapstruct.Mapper;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface LikeMapper {
    LikeResponse entityToResponseDTO(LikePost like);
}
