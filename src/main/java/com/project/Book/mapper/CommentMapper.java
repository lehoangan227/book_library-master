package com.project.Book.mapper;

import com.project.Book.dto.request.CommentRequest;
import com.project.Book.dto.response.CommentResponse;
import com.project.Book.entity.Comment;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface CommentMapper {
    Comment requestDtoToEntity(CommentRequest commentRequest);
    CommentResponse entityToResponseDTO(Comment comment);
    void updateDtoToEntity(CommentRequest commentRequest, @MappingTarget Comment comment);
}
