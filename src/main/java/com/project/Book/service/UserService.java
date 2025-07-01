package com.project.Book.service;

import com.project.Book.dto.request.SearchUserRequest;
import com.project.Book.dto.request.UserCreateRequest;
import com.project.Book.dto.request.UserUpdateRequest;
import com.project.Book.dto.response.PageResponse;
import com.project.Book.dto.response.UserResponse;

import java.util.List;

public interface UserService {
    public UserResponse createUser(UserCreateRequest userCreateRequest);
    public UserResponse updateUser(int userId, UserUpdateRequest userUpdateRequest);
    public UserResponse getUser(int userId);
    public void deleteUser(int userId);
    public PageResponse<UserResponse> getUsers(int pageNo, int pageSize, SearchUserRequest searchUserRequest, List<String> sorts);
}
