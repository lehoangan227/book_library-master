package com.project.Book.controller;

import com.project.Book.config.Translator;
import com.project.Book.dto.request.SearchUserRequest;
import com.project.Book.dto.request.UserCreateRequest;
import com.project.Book.dto.request.UserUpdateRequest;
import com.project.Book.dto.response.ApiResponse;
import com.project.Book.dto.response.PageResponse;
import com.project.Book.dto.response.UserResponse;
import com.project.Book.entity.User;
import com.project.Book.service.UserService;
import com.project.Book.util.excel.BaseExport;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@RestController
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequestMapping("/user")
@Slf4j
@Tag(name = "User Controller")
public class UserController {
    UserService userService;

    @Operation(summary = "create user", description = "Api create user", security = @SecurityRequirement(name = ""))
    @PostMapping("/create")
    public ResponseEntity<ApiResponse<UserResponse>> createUser(@RequestBody @Valid UserCreateRequest userCreateRequest){
        ApiResponse<UserResponse> apiResponse = ApiResponse.<UserResponse>builder()
                .code("user.create.success")
                .message(Translator.toLocale("user.create.success"))
                .data(userService.createUser(userCreateRequest)).build();
        return ResponseEntity.ok(apiResponse);
    }

    @Operation(summary = "update user", description = "Api update user")
    @PutMapping("/update/{userId}")
    @PreAuthorize("@checkPermission.fileRole(#httpServletRequest)")
    public ResponseEntity<ApiResponse<UserResponse>> updateUser(@PathVariable("userId") int userId,
                                                                @RequestBody @Valid UserUpdateRequest userUpdateRequest,
                                                                HttpServletRequest httpServletRequest){
        ApiResponse<UserResponse> apiResponse = ApiResponse.<UserResponse>builder()
                .code("user.update.success")
                .message(Translator.toLocale("user.update.success"))
                .data(userService.updateUser(userId, userUpdateRequest)).build();
        return ResponseEntity.ok(apiResponse);
    }

    @Operation(summary = "get detail user", description = "Api get detail user")
    @GetMapping("/detail/{userId}")
    @PreAuthorize("@checkPermission.fileRole(#httpServletRequest)")
    public ResponseEntity<ApiResponse<UserResponse>> getUser(@PathVariable("userId") int userId, HttpServletRequest httpServletRequest){
        ApiResponse<UserResponse> apiResponse = ApiResponse.<UserResponse>builder()
                .code("user.get.success")
                .message(Translator.toLocale("user.get.success"))
                .data(userService.getUser(userId)).build();
        return ResponseEntity.ok(apiResponse);
    }

    @Operation(summary = "delete user", description = "Api delete user")
    @DeleteMapping("/delete/{userId}")
    @PreAuthorize("@checkPermission.fileRole(#httpServletRequest)")
    public ResponseEntity<ApiResponse> deleteUser(@PathVariable("userId") int userId,HttpServletRequest httpServletRequest){
        userService.deleteUser(userId);
        ApiResponse apiResponse = ApiResponse.builder()
                .code("user.delete.success")
                .message(Translator.toLocale("user.delete.success")).build();
        return ResponseEntity.ok(apiResponse);
    }

    @Operation(summary = "get list users", description = "Api get list users")
    @GetMapping
    @PreAuthorize("@checkPermission.fileRole(#httpServletRequest)")
    public ResponseEntity<ApiResponse<PageResponse<UserResponse>>> getUsers(@RequestParam(defaultValue = "0", required = false) int pageNo,
                                                                            @RequestParam(defaultValue = "100", required = false) int pageSize,
                                                                            @RequestBody(required = false) SearchUserRequest searchUserRequest,
                                                                            @RequestParam(required = false, name = "sorts", defaultValue = "userId:asc")List<String> sorts,
                                                                            HttpServletRequest httpServletRequest){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        log.info(authentication.getAuthorities().toString());
        log.info(httpServletRequest.getRequestURI().replaceAll("^/|/$", "").replaceAll("/", "."));
        ApiResponse<PageResponse<UserResponse>> apiResponse = ApiResponse.<PageResponse<UserResponse>>builder()
                .code("user.getlist.success")
                .message(Translator.toLocale("user.getlist.success"))
                .data(userService.getUsers(pageNo,pageSize,searchUserRequest,sorts))
                .build();
        return ResponseEntity.ok(apiResponse);
    }

    @Operation(summary = "export to excel", description = "Api export to excel file")
    @GetMapping("/export")
    @PreAuthorize("@checkPermission.fileRole(#httpServletRequest)")
    public ResponseEntity<ApiResponse> exportToExcel(HttpServletResponse httpServletResponse,
                                                     HttpServletRequest httpServletRequest) throws IOException {
        httpServletResponse.setContentType("application/octet-stream");
        httpServletResponse.setHeader("Content-Disposition", "attachment; filename=users_" + new SimpleDateFormat("yyyy-MM-dd_HH:mm:ss").format(new Date()) + ".xlsx");
        userService.exportToExcel(httpServletResponse);
        ApiResponse apiResponse = ApiResponse.builder()
                .code("user.export-to-excel.success")
                .message(Translator.toLocale("user.export-to-excel.success")).build();
        return ResponseEntity.ok(apiResponse);
    }

    @Operation(summary = "get profile", description = "get profile")
    @GetMapping("/profile")
    @PreAuthorize("@checkPermission.fileRole(#httpServletRequest)")
    public ResponseEntity<ApiResponse<UserResponse>> getProfile(HttpServletRequest httpServletRequest){
        ApiResponse<UserResponse> apiResponse = ApiResponse.<UserResponse>builder()
                .code("user.get-profile.success")
                .message(Translator.toLocale("user.get-profile.success"))
                .data(userService.getProfile()).build();
        return ResponseEntity.ok(apiResponse);
    }

    @Operation(summary = "update profile", description = "update profile")
    @PutMapping("/update-profile")
    @PreAuthorize("@checkPermission.fileRole(#httpServletRequest)")
    public ResponseEntity<ApiResponse<UserResponse>> updateUser(@RequestBody @Valid UserUpdateRequest userUpdateRequest,
                                                                HttpServletRequest httpServletRequest){
        ApiResponse<UserResponse> apiResponse = ApiResponse.<UserResponse>builder()
                .code("user.update-profile.success")
                .message(Translator.toLocale("user.update-profile.success"))
                .data(userService.updateProfile(userUpdateRequest)).build();
        return ResponseEntity.ok(apiResponse);
    }
}
