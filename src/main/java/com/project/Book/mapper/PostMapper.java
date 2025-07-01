package com.project.Book.mapper;

import com.project.Book.dto.request.PostRequest;
import com.project.Book.dto.response.PostInListResponse;
import com.project.Book.dto.response.PostResponse;
import com.project.Book.entity.Post;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface PostMapper {
    Post requestDtoToEntity(PostRequest postRequest);
    PostResponse entityToResponseDTO(Post post);
    void updateDtoToEntity(PostRequest postRequest,@MappingTarget Post post);
    PostInListResponse entityToListResponseDTO(Post post);
}
