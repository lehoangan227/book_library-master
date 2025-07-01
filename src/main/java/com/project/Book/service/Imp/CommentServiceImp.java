package com.project.Book.service.Imp;

import com.project.Book.dto.request.CommentRequest;
import com.project.Book.dto.request.CommentUpdateRequest;
import com.project.Book.dto.request.SearchCommentRequest;
import com.project.Book.dto.response.CommentResponse;
import com.project.Book.dto.response.PageResponse;
import com.project.Book.entity.Comment;
import com.project.Book.entity.Post;
import com.project.Book.entity.User;
import com.project.Book.exception.AppException;
import com.project.Book.mapper.CommentMapper;
import com.project.Book.repository.CommentRepository;
import com.project.Book.repository.PostRepository;
import com.project.Book.repository.UserRepository;
import com.project.Book.service.CommentService;
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
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class CommentServiceImp implements CommentService {
    CommentRepository commentRepository;
    PostRepository postRepository;
    UserRepository userRepository;
    CommentMapper commentMapper;

    @Override
    public CommentResponse createComment(CommentRequest commentRequest) {
        Comment comment = commentMapper.requestDtoToEntity(commentRequest);
        Post post = postRepository.findByPostIdAndIsDeleteFalse(commentRequest.getPostId())
                .orElseThrow(()->new AppException("error.post.notfound", HttpStatus.NOT_FOUND));
        comment.setPost(post);
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();
        User user = userRepository.findByUsernameAndIsDeleteFalse(username)
                .orElseThrow(()->new AppException("error.user.notfound", HttpStatus.NOT_FOUND));
        comment.setUser(user);
        comment.setCreateAt(LocalDateTime.now());
        comment = commentRepository.save(comment);
        CommentResponse commentResponse = commentMapper.entityToResponseDTO(comment);
        commentResponse.setCommentAuthor(username);
        commentResponse.setPostId(commentRequest.getPostId());
        return commentResponse;
    }

    @Override
    public CommentResponse updateComment(int commentId, CommentUpdateRequest commentUpdateRequest) {
        Comment comment = commentRepository.findByCommentIdAndIsDeleteFalse(commentId)
                .orElseThrow(()->new AppException("error.comment.notfound", HttpStatus.NOT_FOUND));
        comment.setUpdateAt(LocalDateTime.now());
        commentMapper.updateDtoToEntity(commentUpdateRequest, comment);
        comment = commentRepository.save(comment);
        CommentResponse commentResponse = commentMapper.entityToResponseDTO(comment);
        commentResponse.setCommentAuthor(comment.getUser().getUsername());
        commentResponse.setPostId(comment.getPost().getPostId());
        return commentResponse;
    }

    @Override
    @Transactional
    public void deleteComment(int commentId) {
        if(commentRepository.existsByCommentIdAndIsDeleteFalse(commentId)) {
            commentRepository.softDeleteByCommentId(commentId);
        }else{
            throw new AppException("error.comment.notfound", HttpStatus.NOT_FOUND);
        }
    }

    @Override
    public CommentResponse getComment(int commentId) {
        Comment comment = commentRepository.findByCommentIdAndIsDeleteFalse(commentId)
                .orElseThrow(()->new AppException("error.comment.notfound", HttpStatus.NOT_FOUND));
        CommentResponse commentResponse = commentMapper.entityToResponseDTO(comment);
        commentResponse.setCommentAuthor(comment.getUser().getUsername());
        commentResponse.setPostId(comment.getPost().getPostId());
        return commentResponse;
    }

    @Override
    public PageResponse<CommentResponse> getComments(int pageNo, int pageSize, List<String> sorts, SearchCommentRequest searchCommentRequest) {
        Pageable pageable = PageRequest.of(pageNo, pageSize, Sort.by(UtilClass.getOrders(sorts)));
        if(searchCommentRequest == null) {
            searchCommentRequest = new SearchCommentRequest();
        }
        Page<Comment> commentPage = commentRepository.searchComment(pageable, searchCommentRequest);
        List<Comment> comments = commentPage.getContent();
        List<CommentResponse> commentResponses = comments.stream().map(commentMapper::entityToResponseDTO).toList();
        for(int i=0;i<commentResponses.size();i++){
            commentResponses.get(i).setCommentAuthor(comments.get(i).getUser().getUsername());
            commentResponses.get(i).setPostId(comments.get(i).getPost().getPostId());
        }
        return PageResponse.<CommentResponse>builder()
                .pageNo(pageNo)
                .pageSize(pageSize)
                .totalPage(commentPage.getTotalPages())
                .totalElements(commentPage.getTotalElements())
                .items(commentResponses).build();
    }
}
