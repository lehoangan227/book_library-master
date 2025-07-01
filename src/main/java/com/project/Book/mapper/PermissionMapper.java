package com.project.Book.mapper;

import com.project.Book.dto.request.PermissionRequest;
import com.project.Book.dto.response.PermissionResponse;
import com.project.Book.entity.Permission;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface PermissionMapper {
    public Permission requestDTOToEntity(PermissionRequest permissionRequest);
    public PermissionResponse entityToResponseDTO(Permission permission);
    public void updateDTOToEntity(@MappingTarget Permission permission, PermissionRequest permissionRequest);
}
