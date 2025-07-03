package com.project.Book.service.Imp;

import com.project.Book.dto.response.LikeResponse;
import com.project.Book.dto.response.PageResponse;
import com.project.Book.dto.response.PostInListResponse;
import com.project.Book.dto.response.PostResponse;
import com.project.Book.entity.LikePost;
import com.project.Book.entity.Post;
import com.project.Book.entity.User;
import com.project.Book.enums.Role;
import com.project.Book.exception.AppException;
import com.project.Book.mapper.LikeMapper;
import com.project.Book.mapper.PostMapper;
import com.project.Book.repository.LikePostRepository;
import com.project.Book.repository.PostRepository;
import com.project.Book.repository.UserRepository;
import com.project.Book.service.LikeService;
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

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class LikeServiceImp implements LikeService {
    LikePostRepository likePostRepository;
    UserRepository userRepository;
    PostRepository postRepository;
    LikeMapper likeMapper;
    private final PostMapper postMapper;

    @Override
    public LikeResponse LikePost(int postId) {
        int userId = Integer.parseInt(UtilClass.getUserId());
        User user = userRepository.findByUserIdAndIsDeleteFalse(userId)
                .orElseThrow(()->new AppException("error.user.notfound",HttpStatus.NOT_FOUND));
        Post post = postRepository.findByPostIdAndIsDeleteFalse(postId)
                .orElseThrow(()->new AppException("error.post.notfound", HttpStatus.NOT_FOUND));
        LikePost like = LikePost.builder()
                .post(post)
                .user(user)
                .createAt(LocalDateTime.now()).build();
        like = likePostRepository.save(like);
        LikeResponse likeResponse = likeMapper.entityToResponseDTO(like);
        likeResponse.setLikeAuthor(user.getUsername());
        likeResponse.setPostId(post.getPostId());
        return likeResponse;
    }

    @Override
    public void unlikePost(int postId) {
        int userId = Integer.parseInt(UtilClass.getUserId());
        LikePost likePost = likePostRepository.findByUserIdAndPostId(userId,postId)
                .orElseThrow(()->new AppException("error.like.notfound",HttpStatus.NOT_FOUND));
        likePostRepository.delete(likePost);
    }

    @Override
    public Integer getTotalLikes(int postId) {
        if(postRepository.existsByPostIdAndIsDeleteFalse(postId)) {
            return likePostRepository.countLikeByPostId(postId);
        }else{
            throw new AppException("error.post.notfound",HttpStatus.NOT_FOUND);
        }
    }

    @Override
    public PageResponse<PostInListResponse> getPostsLiked(int pageNo, int pageSize, List<String> sorts) {
        int userId = Integer.parseInt(UtilClass.getUserId());
        Pageable pageable = PageRequest.of(pageNo, pageSize, Sort.by(UtilClass.getOrders(sorts)));
        Page<Post> postPage = likePostRepository.getPostsLikedByUserId(userId, pageable);
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
                .items(postInListResponses).build();
    }

    @Override
    public PageResponse<String> getUsersLikedPost(int pageNo, int pageSize, int postId) {
        Pageable pageable = PageRequest.of(pageNo,pageSize);
        Page<User> userPage = likePostRepository.getUsersLikedPostByPostId(postId, pageable);
        List<String> usernames = userPage.getContent().stream().map(User::getUsername).toList();
        return PageResponse.<String>builder()
                .pageNo(pageNo)
                .pageSize(pageSize)
                .totalPage(userPage.getTotalPages())
                .totalElements(userPage.getTotalElements())
                .items(usernames).build();
    }
}
