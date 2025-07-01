package com.project.Book.mapper;

import com.project.Book.dto.request.RoleRequest;
import com.project.Book.dto.request.RoleUpdateRequest;
import com.project.Book.dto.response.RoleInListResponse;
import com.project.Book.dto.response.RoleResponse;
import com.project.Book.entity.Role;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface RoleMapper {
    Role requestDTOToEntity(RoleRequest request);
    RoleResponse entityToResponseDTO(Role role);
    void updateDTOToEntity(@MappingTarget Role role, RoleUpdateRequest request);
    RoleInListResponse entityToRoleInListDTO(Role role);
}
