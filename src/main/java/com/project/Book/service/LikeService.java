package com.project.Book.service;

import com.project.Book.dto.response.LikeResponse;
import com.project.Book.dto.response.PageResponse;
import com.project.Book.dto.response.PostInListResponse;
import com.project.Book.dto.response.PostResponse;

import java.util.List;

public interface LikeService {
    LikeResponse LikePost(int postId);
    void unlikePost(int postId);
    Integer getTotalLikes(int postId);
    PageResponse<PostInListResponse> getPostsLikedByUser(int pageNo, int pageSize, List<String> sorts, int userId);
}
