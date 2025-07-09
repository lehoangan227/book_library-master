package com.project.Book.controller;

import com.project.Book.config.Translator;
import com.project.Book.dto.request.RoleRequest;
import com.project.Book.dto.request.RoleUpdateRequest;
import com.project.Book.dto.response.*;
import com.project.Book.service.RoleService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequestMapping("/role")
@Tag(name = "Role Controller")
public class RoleController {
    RoleService roleService;

    @Operation(summary = "create role", description = "Api create role")
    @PostMapping("/create")
    @PreAuthorize("@checkPermission.fileRole(#httpServletRequest)")
    public ResponseEntity<ApiResponse<RoleResponse>> createRole(@RequestBody @Valid RoleRequest roleRequest,
                                                                HttpServletRequest httpServletRequest) {
        ApiResponse<RoleResponse> apiResponse = ApiResponse.<RoleResponse>builder()
                .code("role.create.success")
                .message(Translator.toLocale("role.create.success"))
                .data(roleService.createRole(roleRequest))
                .build();
        return ResponseEntity.ok(apiResponse);
    }

    @Operation(summary = "update role", description = "Api update role")
    @PutMapping("/update/{roleId}")
    @PreAuthorize("@checkPermission.fileRole(#httpServletRequest)")
    public ResponseEntity<ApiResponse<RoleResponse>> updateRole(@PathVariable("roleId") int roleId,
                                                                @RequestBody @Valid RoleUpdateRequest roleUpdateRequest,
                                                                HttpServletRequest httpServletRequest) {
        ApiResponse<RoleResponse> apiResponse = ApiResponse.<RoleResponse>builder()
                .code("role.update.success")
                .message(Translator.toLocale("role.update.success"))
                .data(roleService.updateRole(roleId, roleUpdateRequest))
                .build();
        return ResponseEntity.ok(apiResponse);
    }

    @Operation(summary = "delete role", description = "Api delete role")
    @DeleteMapping("/delete/{roleId}")
    @PreAuthorize("@checkPermission.fileRole(#httpServletRequest)")
    public ResponseEntity<ApiResponse> deleteRole(@PathVariable("roleId") int roleId, HttpServletRequest httpServletRequest) {
        roleService.deleteRole(roleId);
        ApiResponse apiResponse = ApiResponse.builder()
                .code("role.delete.success")
                .message(Translator.toLocale("role.delete.success"))
                .build();
        return ResponseEntity.ok(apiResponse);
    }

    @Operation(summary = "get detail role", description = "Api get detail role")
    @GetMapping("/detail/{roleId}")
    @PreAuthorize("@checkPermission.fileRole(#httpServletRequest)")
    public ResponseEntity<ApiResponse<RoleResponse>> getRole(@PathVariable("roleId") int roleId, HttpServletRequest httpServletRequest) {
        ApiResponse<RoleResponse> apiResponse = ApiResponse.<RoleResponse>builder()
                .code("role.get.success")
                .message(Translator.toLocale("role.get.success"))
                .data(roleService.getRole(roleId))
                .build();
        return ResponseEntity.ok(apiResponse);
    }

    @Operation(summary = "get list roles", description = "Api get list roles")
    @GetMapping
    @PreAuthorize("@checkPermission.fileRole(#httpServletRequest)")
    public ResponseEntity<ApiResponse<PageResponse<RoleInListResponse>>> getRoles(@RequestParam(name ="pageNo", defaultValue = "0", required = false)int pageNo,
                                                                                  @RequestParam(name = "pageSize", defaultValue = "10", required = false)int pageSize,
                                                                                  @RequestParam(name = "roleName", required = false)String roleName,
                                                                                  @RequestParam(name = "sorts", defaultValue = "roleId:asc", required = false) List<String> sorts,
                                                                                  HttpServletRequest httpServletRequest) {
        ApiResponse<PageResponse<RoleInListResponse>> apiResponse = ApiResponse.<PageResponse<RoleInListResponse>>builder()
                .code("role.getlist.success")
                .message(Translator.toLocale("role.getlist.success"))
                .data(roleService.getRoles(pageNo, pageSize, roleName, sorts)).build();
        return ResponseEntity.ok(apiResponse);
    }
}
