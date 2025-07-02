package com.project.Book.service.Imp;

import com.project.Book.dto.request.SearchUserRequest;
import com.project.Book.dto.request.UserCreateRequest;
import com.project.Book.dto.request.UserUpdateRequest;
import com.project.Book.dto.response.PageResponse;
import com.project.Book.dto.response.UserResponse;
import com.project.Book.entity.User;
import com.project.Book.enums.Role;
import com.project.Book.exception.AppException;
import com.project.Book.mapper.UserMapper;
import com.project.Book.repository.UserRepository;
import com.project.Book.service.UserService;
import com.project.Book.util.UtilClass;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class UserServiceImp implements UserService {
    UserRepository userRepository;
    UserMapper userMapper;

    @Override
    @Transactional
    public UserResponse createUser(UserCreateRequest userCreateRequest) throws AppException {
        if(userRepository.existsByUsernameAndIsDeleteFalse(userCreateRequest.getUsername())){
            throw new AppException("error.username.existed", HttpStatus.CONFLICT);
        }
        if(userRepository.existsByEmailAndIsDeleteFalse(userCreateRequest.getEmail())){
            throw new AppException("error.email.used",HttpStatus.CONFLICT);
        }
        userCreateRequest.setPassword(UtilClass.hashPassword(userCreateRequest.getPassword()));
        User user = userRepository.save(userMapper.createDtoToEntity(userCreateRequest));
        userRepository.setDefaultRole(user.getUserId(), Role.GROUP_USER.getRoleCode());
        return userMapper.entityToResponseDTO(user);
    }

    @Override
    public UserResponse updateUser(int userId, UserUpdateRequest userUpdateRequest) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        boolean isUser = auth.getAuthorities().stream()
                .anyMatch(grantedAuthority -> grantedAuthority.getAuthority().equals(Role.GROUP_USER.getRoleCode()));
        if(isUser&&(Integer.parseInt(UtilClass.getUserId())!=userId)){
            throw new AccessDeniedException("access denied");
        }
        User user = userRepository.findByUserIdAndIsDeleteFalse(userId).orElseThrow(()->new AppException("error.user.notfound", HttpStatus.NOT_FOUND));
        if(userRepository.existsByEmailAndIsDeleteFalse(userUpdateRequest.getEmail())&&!userUpdateRequest.getEmail().equals(user.getEmail())){
            throw new AppException("error.email.used", HttpStatus.CONFLICT);
        }
        userMapper.updateDtoToEntity(user, userUpdateRequest);
        return userMapper.entityToResponseDTO(userRepository.save(user));
    }

    @Override
    public UserResponse getUser(int userId) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        boolean isUser = auth.getAuthorities().stream()
                .anyMatch(grantedAuthority -> grantedAuthority.getAuthority().equals(Role.GROUP_USER.getRoleCode()));
        if(isUser&&(Integer.parseInt(UtilClass.getUserId())!=userId)){
            throw new AccessDeniedException("access denied");
        }
        User user  = userRepository.findByUserIdAndIsDeleteFalse(userId).orElseThrow(()->new AppException("error.user.notfound", HttpStatus.NOT_FOUND));
        return userMapper.entityToResponseDTO(user);
    }

    @Override
    @Transactional
    public void deleteUser(int userId) {
        if(userRepository.existsByUserIdAndIsDeleteFalse(userId)){
            userRepository.softDeleteByUserId(userId);
        }
        else{
            throw new AppException("error.user.notfound", HttpStatus.NOT_FOUND);
        }
    }

    @Override
    public PageResponse<UserResponse> getUsers(int pageNo, int pageSize, SearchUserRequest searchUserRequest, List<String> sorts) {
        Pageable pageable = PageRequest.of(pageNo, pageSize, Sort.by(UtilClass.getOrders(sorts)));
        if(searchUserRequest == null){
            searchUserRequest = new SearchUserRequest();
        }
        Page<User> users = userRepository.searchUser(pageable, searchUserRequest);
        return PageResponse.<UserResponse>builder()
                .pageNo(pageNo)
                .pageSize(pageSize)
                .totalPage(users.getTotalPages())
                .totalElements(users.getTotalElements())
                .items(users.getContent().stream().map(userMapper::entityToResponseDTO).toList())
                .build();
    }
}
