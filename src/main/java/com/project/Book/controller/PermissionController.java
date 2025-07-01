package com.project.Book.controller;

import com.project.Book.config.Translator;
import com.project.Book.dto.request.PermissionRequest;
import com.project.Book.dto.response.ApiResponse;
import com.project.Book.dto.response.PageResponse;
import com.project.Book.dto.response.PermissionResponse;
import com.project.Book.dto.response.RoleInListResponse;
import com.project.Book.service.PermissionService;
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
@RequestMapping("/permission")
public class PermissionController {
    PermissionService permissionService;
    @PostMapping("/create")
    @PreAuthorize("@checkPermission.fileRole(#httpServletRequest)")
    public ResponseEntity<ApiResponse<PermissionResponse>> createPermission(@RequestBody @Valid PermissionRequest permissionRequest,
                                                                            HttpServletRequest httpServletRequest) {
        ApiResponse<PermissionResponse> apiResponse = ApiResponse.<PermissionResponse>builder()
                .code("permission.create.success")
                .message(Translator.toLocale("permission.create.success"))
                .data(permissionService.createPermission(permissionRequest))
                .build();
        return ResponseEntity.ok(apiResponse);
    }

    @PutMapping("/update/{perId}")
    @PreAuthorize("@checkPermission.fileRole(#httpServletRequest)")
    public ResponseEntity<ApiResponse<PermissionResponse>> updatePermission(@PathVariable("perId") int perId,
                                                                            @RequestBody @Valid PermissionRequest permissionRequest,
                                                                            HttpServletRequest httpServletRequest) {
        ApiResponse<PermissionResponse> apiResponse = ApiResponse.<PermissionResponse>builder()
                .code("permission.update.success")
                .message(Translator.toLocale("permission.update.success"))
                .data(permissionService.updatePermission(perId, permissionRequest))
                .build();
        return ResponseEntity.ok(apiResponse);
    }

    @DeleteMapping("/delete/{perId}")
    @PreAuthorize("@checkPermission.fileRole(#httpServletRequest)")
    public ResponseEntity<ApiResponse> deletePermission(@PathVariable("perId") int perId, HttpServletRequest httpServletRequest) {
        permissionService.deletePermission(perId);
        ApiResponse apiResponse = ApiResponse.builder()
                .code("permission.delete.success")
                .message(Translator.toLocale("permission.delete.success"))
                .build();
        return ResponseEntity.ok(apiResponse);
    }

    @GetMapping("/detail/{perId}")
    @PreAuthorize("@checkPermission.fileRole(#httpServletRequest)")
    public ResponseEntity<ApiResponse<PermissionResponse>> getPermission(@PathVariable("perId") int perId,
                                                                         HttpServletRequest httpServletRequest) {
        ApiResponse<PermissionResponse> apiResponse = ApiResponse.<PermissionResponse>builder()
                .code("permission.get.success")
                .message(Translator.toLocale("permission.get.success"))
                .data(permissionService.getPermission(perId))
                .build();
        return ResponseEntity.ok(apiResponse);
    }

    @GetMapping
    @PreAuthorize("@checkPermission.fileRole(#httpServletRequest)")
    public ResponseEntity<ApiResponse<PageResponse<PermissionResponse>>> getPermissions(@RequestParam(name ="pageNo", defaultValue = "0", required = false)int pageNo,
                                                                                        @RequestParam(name = "pageSize", defaultValue = "10", required = false)int pageSize,
                                                                                        @RequestParam(name = "perName", required = false)String perName,
                                                                                        @RequestParam(name = "sorts", defaultValue = "perId:asc", required = false)List<String> sorts,
                                                                                        HttpServletRequest httpServletRequest){
        ApiResponse<PageResponse<PermissionResponse>> apiResponse = ApiResponse.<PageResponse<PermissionResponse>>builder()
                .code("permission.getlist.success")
                .message(Translator.toLocale("permission.getlist.success"))
                .data(permissionService.getPermissions(pageNo, pageSize, perName, sorts)).build();
        return ResponseEntity.ok(apiResponse);
    }

    @GetMapping("/find-by-user/{userId}")
    @PreAuthorize("@checkPermission.fileRole(#httpServletRequest)")
    public ResponseEntity<ApiResponse<List<PermissionResponse>>> getPermissionsByUser(@PathVariable("userId") int userId,
                                                                                      HttpServletRequest httpServletRequest){
        ApiResponse<List<PermissionResponse>> apiResponse = ApiResponse.<List<PermissionResponse>>builder()
                .code("permission.getlistbyuser.success")
                .message(Translator.toLocale("permission.getlistbyuser.success"))
                .data(permissionService.getPermissionsByUser(userId))
                .build();
        return ResponseEntity.ok(apiResponse);
    }

}
