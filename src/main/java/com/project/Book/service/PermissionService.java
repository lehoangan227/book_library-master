package com.project.Book.service;

import com.project.Book.dto.request.PermissionRequest;
import com.project.Book.dto.response.PageResponse;
import com.project.Book.dto.response.PermissionResponse;

import java.util.List;

public interface PermissionService {
    PermissionResponse createPermission(PermissionRequest permissionRequest);
    PermissionResponse updatePermission(int perId, PermissionRequest permissionRequest);
    void deletePermission(int perId);
    PermissionResponse getPermission(int perId);
    PageResponse<PermissionResponse> getPermissions(int pageNo, int pageSize, String perName, List<String> sorts);
    List<PermissionResponse> getPermissionsByUser(int userId);
}
