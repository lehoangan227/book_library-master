package com.project.Book.service;

import com.project.Book.dto.request.SearchUserRequest;
import com.project.Book.dto.request.UserCreateRequest;
import com.project.Book.dto.request.UserUpdateRequest;
import com.project.Book.dto.response.PageResponse;
import com.project.Book.dto.response.UserResponse;
import com.project.Book.entity.User;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.List;

public interface UserService {
    public UserResponse createUser(UserCreateRequest userCreateRequest);
    public UserResponse updateUser(int userId, UserUpdateRequest userUpdateRequest);
    public UserResponse getUser(int userId);
    public void deleteUser(int userId);
    public PageResponse<UserResponse> getUsers(int pageNo, int pageSize, SearchUserRequest searchUserRequest, List<String> sorts);
    void exportToExcel(HttpServletResponse httpServletResponse) throws IOException;
    public UserResponse getProfile();
    public UserResponse updateProfile(UserUpdateRequest userUpdateRequest);
    Integer getTotalUsers();
}
