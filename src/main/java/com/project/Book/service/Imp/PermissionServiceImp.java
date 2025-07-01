package com.project.Book.service.Imp;

import com.project.Book.dto.request.PermissionRequest;
import com.project.Book.dto.response.CategoryResponse;
import com.project.Book.dto.response.PageResponse;
import com.project.Book.dto.response.PermissionResponse;
import com.project.Book.entity.Category;
import com.project.Book.entity.Permission;
import com.project.Book.exception.AppException;
import com.project.Book.mapper.PermissionMapper;
import com.project.Book.repository.PermissionRepository;
import com.project.Book.repository.UserRepository;
import com.project.Book.service.PermissionService;
import com.project.Book.util.UtilClass;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class PermissionServiceImp implements PermissionService {
    PermissionRepository permissionRepository;
    UserRepository userRepository;
    PermissionMapper permissionMapper;
    @Override
    public PermissionResponse createPermission(PermissionRequest permissionRequest) {
        if(permissionRepository.existsByPerCodeAndIsDeleteFalse(permissionRequest.getPerCode())){
            throw new AppException("error.code.existed", HttpStatus.CONFLICT);
        }
        return permissionMapper.entityToResponseDTO(permissionRepository.save(permissionMapper.requestDTOToEntity(permissionRequest)));
    }

    @Override
    public PermissionResponse updatePermission(int perId, PermissionRequest permissionRequest) {
        Permission permission = permissionRepository.findByPerIdAndIsDeleteFalse(perId).orElseThrow(()->new AppException("error.permission.notfound", HttpStatus.NOT_FOUND));
        if(permissionRepository.existsByPerCodeAndIsDeleteFalse(permissionRequest.getPerCode())&&!permissionRequest.getPerCode().equals(permission.getPerCode())){
            throw new AppException("error.code.existed", HttpStatus.CONFLICT);
        }
        permissionMapper.updateDTOToEntity(permission, permissionRequest);
        return permissionMapper.entityToResponseDTO(permissionRepository.save(permission));
    }

    @Override
    @Transactional
    public void deletePermission(int perId) {
        if(permissionRepository.existsByPerIdAndIsDeleteFalse(perId)){
            permissionRepository.softDeleteByPerId(perId);
        }
        else{
            throw new AppException("error.permission.notfound", HttpStatus.NOT_FOUND);
        }
    }

    @Override
    public PermissionResponse getPermission(int perId) {
        Permission permission = permissionRepository.findByPerIdAndIsDeleteFalse(perId).orElseThrow(()->new AppException("error.permission.notfound", HttpStatus.NOT_FOUND));
        return permissionMapper.entityToResponseDTO(permission);
    }

    @Override
    public PageResponse<PermissionResponse> getPermissions(int pageNo, int pageSize, String perName, List<String> sorts) {
        Pageable pageable = PageRequest.of(pageNo, pageSize, Sort.by(UtilClass.getOrders(sorts)));
        Page<Permission> permissions = permissionRepository.searchPermissions(pageable, perName);
        return PageResponse.<PermissionResponse>builder()
                .pageNo(pageNo)
                .pageSize(pageSize)
                .totalPage(permissions.getTotalPages())
                .totalElements(permissions.getTotalElements())
                .items(permissions.getContent().stream().map(permissionMapper::entityToResponseDTO).toList())
                .build();
    }

    @Override
    public List<PermissionResponse> getPermissionsByUser(int userId) {
        if(!userRepository.existsByUserIdAndIsDeleteFalse(userId)){
            throw new AppException("error.user.notfound", HttpStatus.NOT_FOUND);
        }
        Set<Permission> permissions = permissionRepository.getPermissionsByUser(userId);
        return permissions.stream().map(permissionMapper::entityToResponseDTO).toList();
    }
}
