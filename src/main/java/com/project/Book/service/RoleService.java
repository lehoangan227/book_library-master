package com.project.Book.service;

import com.project.Book.dto.request.RoleRequest;
import com.project.Book.dto.request.RoleUpdateRequest;
import com.project.Book.dto.response.PageResponse;
import com.project.Book.dto.response.RoleInListResponse;
import com.project.Book.dto.response.RoleResponse;

import java.util.List;


public interface RoleService {
    RoleResponse createRole(RoleRequest roleRequest);
    RoleResponse updateRole(int roleId, RoleUpdateRequest roleUpdateRequest);
    void deleteRole(int roleId);
    RoleResponse getRole(int roleId);
    PageResponse<RoleInListResponse> getRoles(int pageNo, int pageSize, String roleName, List<String> sorts);
}
