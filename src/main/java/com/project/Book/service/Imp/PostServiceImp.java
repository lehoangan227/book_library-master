package com.project.Book.service.Imp;

import com.project.Book.dto.request.PostRequest;
import com.project.Book.dto.request.SearchPostRequest;
import com.project.Book.dto.response.PageResponse;
import com.project.Book.dto.response.PostInListResponse;
import com.project.Book.dto.response.PostResponse;
import com.project.Book.entity.Post;
import com.project.Book.entity.User;
import com.project.Book.exception.AppException;
import com.project.Book.mapper.PostMapper;
import com.project.Book.repository.PostRepository;
import com.project.Book.repository.UserRepository;
import com.project.Book.service.PostService;
import com.project.Book.util.UtilClass;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class PostServiceImp implements PostService {
    PostRepository postRepository;
    UserRepository userRepository;
    PostMapper postMapper;

    @Override
    public PostResponse createPost(PostRequest postRequest) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();
        User user = userRepository.findByUsernameAndIsDeleteFalse(username)
                .orElseThrow(()->new AppException("error.user.notfound", HttpStatus.NOT_FOUND));
        Post post = postMapper.requestDtoToEntity(postRequest);
        post.setUser(user);
        post.setCreateAt(LocalDateTime.now());
        post = postRepository.save(post);
        PostResponse postResponse = postMapper.entityToResponseDTO(post);
        postResponse.setPostAuthor(username);
        return postResponse;
    }

    @Override
    public PostResponse updatePost(int postId, PostRequest postRequest) {
        Post post = postRepository.findByPostIdAndIsDeleteFalse(postId)
                .orElseThrow(()->new AppException("error.post.notfound", HttpStatus.NOT_FOUND));
        postMapper.updateDtoToEntity(postRequest, post);
        post.setUpdateAt(LocalDateTime.now());
        post = postRepository.save(post);
        PostResponse postResponse = postMapper.entityToResponseDTO(post);
        postResponse.setPostAuthor(post.getUser().getUsername());
        return postResponse;
    }

    @Override
    @Transactional
    public void deletePost(int postId) {
        if(postRepository.existsByPostIdAndIsDeleteFalse(postId)){
            postRepository.softDeleteByPostId(postId);
        }else{
            throw new AppException("error.post.notfound", HttpStatus.NOT_FOUND);
        }
    }

    @Override
    public PostResponse getPost(int postId) {
        Post post = postRepository.findByPostIdAndIsDeleteFalse(postId)
                .orElseThrow(()->new AppException("error.post.notfound", HttpStatus.NOT_FOUND));
        PostResponse postResponse = postMapper.entityToResponseDTO(post);
        postResponse.setPostAuthor(post.getUser().getUsername());
        return postResponse;
    }

    @Override
    public PageResponse<PostInListResponse> getPosts(int pageNo, int pageSize, List<String> sorts, SearchPostRequest searchPostRequest) {
        Pageable pageable = PageRequest.of(pageNo, pageSize, Sort.by(UtilClass.getOrders(sorts)));
        if(searchPostRequest==null){
            searchPostRequest = new SearchPostRequest();
        }
        Page<Post> postPage = postRepository.searchPost(pageable, searchPostRequest);
        List<Post> posts = postPage.getContent();
        List<PostInListResponse> postInListResponses = posts.stream().map(postMapper::entityToListResponseDTO).toList();
        for(int i=0;i<postInListResponses.size();i++){
            postInListResponses.get(i).setPostAuthor(posts.get(i).getUser().getUsername());
        }
        return PageResponse.<PostInListResponse>builder()
                .pageNo(pageNo)
                .pageSize(pageSize)
                .totalPage(postPage.getTotalPages())
                .totalElements(postPage.getTotalElements())
                .items(postInListResponses)
                .build();
    }
}
