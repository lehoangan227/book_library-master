package com.project.Book.mapper;

import com.project.Book.dto.request.UserCreateRequest;
import com.project.Book.dto.request.UserUpdateRequest;
import com.project.Book.dto.response.UserResponse;
import com.project.Book.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface UserMapper {
    User createDtoToEntity(UserCreateRequest userCreateRequest);
    UserResponse entityToResponseDTO(User user);
    void updateDtoToEntity(@MappingTarget User user, UserUpdateRequest userUpdateRequest);
}
