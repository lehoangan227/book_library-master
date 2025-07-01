package com.project.Book.controller;

import com.project.Book.config.Translator;
import com.project.Book.dto.response.*;
import com.project.Book.service.LikeService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/like")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class LikeController {
    LikeService likeService;
    @PostMapping("/create/{postId}")
    public ResponseEntity<ApiResponse<LikeResponse>> likePost(@PathVariable("postId")int postId) {
        ApiResponse<LikeResponse> apiResponse = ApiResponse.<LikeResponse>builder()
                .code("like.create.success")
                .message(Translator.toLocale("like.create.success"))
                .data(likeService.LikePost(postId))
                .build();
        return ResponseEntity.ok(apiResponse);
    }

    @DeleteMapping("/delete/{postId}")
    public ResponseEntity<ApiResponse> unlikePost(@PathVariable("postId")int postId){
        likeService.unlikePost(postId);
        ApiResponse apiResponse = ApiResponse.builder()
                .code("like.delete.success")
                .message(Translator.toLocale("like.delete.success")).build();
        return ResponseEntity.ok(apiResponse);
    }

    @GetMapping("/total-likes/{postId}")
    public ResponseEntity<ApiResponse<Integer>> getTotalLikesByPost(@PathVariable("postId")int postId){
        ApiResponse<Integer> apiResponse = ApiResponse.<Integer>builder()
                .code("like.get-total.success")
                .message(Translator.toLocale("like.get-total.success"))
                .data(likeService.getTotalLikes(postId)).build();
        return ResponseEntity.ok(apiResponse);
    }

    @GetMapping("/get-posts/{userId}")
    public ResponseEntity<ApiResponse<PageResponse<PostInListResponse>>> getPostsLikedByUser(@RequestParam(name = "pageNo", defaultValue = "0", required = false) int pageNo,
                                                                                             @RequestParam(name = "pageSize", defaultValue = "2",required = false) int pageSize,
                                                                                             @RequestParam(name = "sorts", required = false) List<String> sorts,
                                                                                             @PathVariable("userId")int userId){
        ApiResponse<PageResponse<PostInListResponse>> apiResponse = ApiResponse.<PageResponse<PostInListResponse>>builder()
                .code("like.get-post-liked-by-user.success")
                .message(Translator.toLocale("like.get-post-liked-by-user.success"))
                .data(likeService.getPostsLikedByUser(pageNo, pageSize, sorts, userId)).build();
        return ResponseEntity.ok(apiResponse);
    }
}
