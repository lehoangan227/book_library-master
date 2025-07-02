package com.project.Book.service.Imp;

import com.project.Book.dto.request.CommentRequest;
import com.project.Book.dto.request.SearchCommentRequest;
import com.project.Book.dto.response.CommentResponse;
import com.project.Book.dto.response.PageResponse;
import com.project.Book.entity.Comment;
import com.project.Book.entity.Post;
import com.project.Book.entity.User;
import com.project.Book.enums.Role;
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
import java.util.List;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class CommentServiceImp implements CommentService {
    CommentRepository commentRepository;
    PostRepository postRepository;
    UserRepository userRepository;
    CommentMapper commentMapper;

    @Override
    public CommentResponse createComment(CommentRequest commentRequest, int postId) {
        Comment comment = commentMapper.requestDtoToEntity(commentRequest);
        Post post = postRepository.findByPostIdAndIsDeleteFalse(postId)
                .orElseThrow(()->new AppException("error.post.notfound", HttpStatus.NOT_FOUND));
        comment.setPost(post);
        int userId = Integer.parseInt(UtilClass.getUserId());
        User user = userRepository.findByUserIdAndIsDeleteFalse(userId)
                .orElseThrow(()->new AppException("error.user.notfound", HttpStatus.NOT_FOUND));
        comment.setUser(user);
        comment.setCreateAt(LocalDateTime.now());
        comment = commentRepository.save(comment);
        CommentResponse commentResponse = commentMapper.entityToResponseDTO(comment);
        commentResponse.setCommentAuthor(user.getUsername());
        commentResponse.setPostId(postId);
        return commentResponse;
    }

    @Override
    public CommentResponse updateComment(int commentId, CommentRequest commentRequest) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        boolean isUser = auth.getAuthorities().stream().anyMatch(grantedAuthority -> grantedAuthority.getAuthority().equals(Role.GROUP_USER.getRoleCode()));
        if(isUser&&!commentRepository.userHasComment(commentId, Integer.parseInt(UtilClass.getUserId()))){
            throw new AppException("error.comment.notfound", HttpStatus.NOT_FOUND);
        }
        Comment comment = commentRepository.findByCommentIdAndIsDeleteFalse(commentId)
                .orElseThrow(()->new AppException("error.comment.notfound", HttpStatus.NOT_FOUND));
        comment.setUpdateAt(LocalDateTime.now());
        commentMapper.updateDtoToEntity(commentRequest, comment);
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
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            boolean isUser = auth.getAuthorities().stream().anyMatch(grantedAuthority -> grantedAuthority.getAuthority().equals(Role.GROUP_USER.getRoleCode()));
            if(isUser&&!commentRepository.userHasComment(commentId, Integer.parseInt(UtilClass.getUserId()))){
                throw new AppException("error.comment.notfound", HttpStatus.NOT_FOUND);
            }
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
    public PageResponse<CommentResponse> getComments(int pageNo, int pageSize, List<String> sorts, SearchCommentRequest searchCommentRequest, int postId) {
        Pageable pageable = PageRequest.of(pageNo, pageSize, Sort.by(UtilClass.getOrders(sorts)));
        if(searchCommentRequest == null) {
            searchCommentRequest = new SearchCommentRequest();
        }
        Page<Comment> commentPage = commentRepository.searchComment(pageable, searchCommentRequest, postId);
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
