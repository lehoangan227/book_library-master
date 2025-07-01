package com.project.Book.service;

import com.project.Book.dto.request.CommentRequest;
import com.project.Book.dto.request.CommentUpdateRequest;
import com.project.Book.dto.request.SearchCommentRequest;
import com.project.Book.dto.response.CommentResponse;
import com.project.Book.dto.response.PageResponse;

import java.util.List;

public interface CommentService {
    CommentResponse createComment(CommentRequest commentRequest);
    CommentResponse updateComment(int commentId, CommentUpdateRequest commentUpdateRequest);
    void deleteComment(int commentId);
    CommentResponse getComment(int commentId);
    PageResponse<CommentResponse> getComments(int pageNo, int pageSize, List<String> sorts, SearchCommentRequest searchCommentRequest);
}
