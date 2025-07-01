package com.project.Book.service.Imp;

import com.project.Book.dto.request.RoleRequest;
import com.project.Book.dto.request.RoleUpdateRequest;
import com.project.Book.dto.response.PageResponse;
import com.project.Book.dto.response.RoleInListResponse;
import com.project.Book.dto.response.RoleResponse;
import com.project.Book.entity.Permission;
import com.project.Book.entity.Role;
import com.project.Book.entity.User;
import com.project.Book.exception.AppException;
import com.project.Book.mapper.RoleMapper;
import com.project.Book.repository.PermissionRepository;
import com.project.Book.repository.RoleRepository;
import com.project.Book.repository.UserRepository;
import com.project.Book.service.RoleService;
import com.project.Book.util.UtilClass;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Repository
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class RoleServiceImp implements RoleService {
    RoleRepository roleRepository;
    UserRepository userRepository;
    PermissionRepository permissionRepository;
    RoleMapper roleMapper;

    @Override
    @Transactional
    public RoleResponse createRole(RoleRequest roleRequest) {
        if(roleRepository.existsByRoleCodeAndIsDeleteFalse((roleRequest.getRoleCode()))){
            throw new AppException("error.code.existed", HttpStatus.CONFLICT);
        }
        Role role = roleMapper.requestDTOToEntity(roleRequest);
        List<User> users = new ArrayList<>();
        List<Permission> permissions = new ArrayList<>();
        if(roleRequest.getUserIds()!=null && !roleRequest.getUserIds().isEmpty()){
            users = userRepository.findAllByUserIdInAndIsDeleteFalse(roleRequest.getUserIds());
            if(users.size()!=roleRequest.getUserIds().size()){
                throw new AppException("error.user.notfound", HttpStatus.NOT_FOUND);
            }
            role.setUsers(users);
        }
        if(roleRequest.getPerIds()!=null&&!roleRequest.getPerIds().isEmpty()){
            permissions = permissionRepository.findAllByPerIdInAndIsDeleteFalse(roleRequest.getPerIds());
            if(permissions.size()!=roleRequest.getPerIds().size()){
                throw new AppException("error.permission.notfound", HttpStatus.NOT_FOUND);
            }
            role.setPermissions(permissions);
        }
        role = roleRepository.save(role);
        RoleResponse roleResponse = roleMapper.entityToResponseDTO(role);
        roleResponse.setUsernames(new ArrayList<>(users.stream().map(User::getUsername).toList()));
        roleResponse.setPerCodes(new ArrayList<>(permissions.stream().map(Permission::getPerCode).toList()));
        return roleResponse;
    }

    @Override
    @Transactional
    public RoleResponse updateRole(int roleId, RoleUpdateRequest roleUpdateRequest) {
        Role role = roleRepository.findByRoleIdAndIsDeleteFalse(roleId).orElseThrow(()->new AppException("error.role.notfound", HttpStatus.NOT_FOUND));
        if(roleRepository.existsByRoleCodeAndIsDeleteFalse(roleUpdateRequest.getRoleCode())&&!role.getRoleCode().equals(roleUpdateRequest.getRoleCode())){
            throw new AppException("error.code.existed", HttpStatus.CONFLICT);
        }
        roleMapper.updateDTOToEntity(role,roleUpdateRequest);
        List<User> users = new ArrayList<>();
        List<Permission> permissions = new ArrayList<>();
        if(roleUpdateRequest.getUserIds()!=null && !roleUpdateRequest.getUserIds().isEmpty()){
            users = userRepository.findAllByUserIdInAndIsDeleteFalse(roleUpdateRequest.getUserIds());
            if(users.size()!=roleUpdateRequest.getUserIds().size()){
                throw new AppException("error.user.notfound", HttpStatus.NOT_FOUND);
            }
            role.setUsers(users);
        }else{
            users = role.getUsers();
        }
        if(roleUpdateRequest.getPerIds()!=null&&!roleUpdateRequest.getPerIds().isEmpty()){
            permissions = permissionRepository.findAllByPerIdInAndIsDeleteFalse(roleUpdateRequest.getPerIds());
            if(permissions.size()!=roleUpdateRequest.getPerIds().size()){
                throw new AppException("error.permission.notfound", HttpStatus.NOT_FOUND);
            }
            role.setPermissions(permissions);
        }else{
            permissions = role.getPermissions();
        }
        role = roleRepository.save(role);
        RoleResponse roleResponse = roleMapper.entityToResponseDTO(role);
        roleResponse.setUsernames(new ArrayList<>(users.stream().map(User::getUsername).toList()));
        roleResponse.setPerCodes(new ArrayList<>(permissions.stream().map(Permission::getPerCode).toList()));
        return roleResponse;
    }

    @Override
    @Transactional
    public void deleteRole(int roleId) {
        if(roleRepository.existsByRoleIdAndIsDeleteFalse(roleId)){
            roleRepository.softDeleteByRoleId(roleId);
        }else{
            throw new AppException("error.role.notfound", HttpStatus.NOT_FOUND);
        }
    }

    @Override
    public RoleResponse getRole(int roleId) {
        Role role = roleRepository.findByRoleIdAndIsDeleteFalse(roleId).orElseThrow(()->new AppException("error.role.notfound", HttpStatus.NOT_FOUND));
        RoleResponse roleResponse = roleMapper.entityToResponseDTO(role);
        roleResponse.setUsernames(new ArrayList<>(role.getUsers().stream().map(User::getUsername).toList()));
        roleResponse.setPerCodes(new ArrayList<>(role.getPermissions().stream().map(Permission::getPerCode).toList()));
        return roleResponse;
    }

    @Override
    public PageResponse<RoleInListResponse> getRoles(int pageNo, int pageSize, String roleName, List<String> sorts) {
        Pageable pageable = PageRequest.of(pageNo, pageSize, Sort.by(UtilClass.getOrders(sorts)));
        Page<Role> roles = roleRepository.searchRoles(pageable, roleName);
        return PageResponse.<RoleInListResponse>builder()
                .pageNo(pageNo)
                .pageSize(pageSize)
                .totalPage(roles.getTotalPages())
                .totalElements(roles.getTotalElements())
                .items(roles.getContent().stream().map(roleMapper::entityToRoleInListDTO).toList())
                .build();
    }
}
