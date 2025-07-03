package com.project.Book.service;

import com.project.Book.dto.request.PostRequest;
import com.project.Book.dto.request.SearchPostRequest;
import com.project.Book.dto.response.PageResponse;
import com.project.Book.dto.response.PostInListResponse;
import com.project.Book.dto.response.PostResponse;

import java.util.List;

public interface PostService {
    PostResponse createPost(PostRequest postRequest);
    PostResponse updatePost(int postId, PostRequest postRequest);
    void deletePost(int postId);
    PostResponse getPost(int postId);
    PageResponse<PostInListResponse> getPosts(int pageNo, int pageSize, List<String> sorts, SearchPostRequest searchPostRequest);
    List<PostResponse> getTop5Posts();
}
