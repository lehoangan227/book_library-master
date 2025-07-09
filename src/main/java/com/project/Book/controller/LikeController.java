package com.project.Book.controller;

import com.project.Book.config.Translator;
import com.project.Book.dto.response.*;
import com.project.Book.service.LikeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/like")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Tag(name = "Like Controller")
public class LikeController {
    LikeService likeService;

    @Operation(summary = "like post", description = "Api like post")
    @PostMapping("/create/{postId}")
    public ResponseEntity<ApiResponse<LikeResponse>> likePost(@PathVariable("postId")int postId) {
        ApiResponse<LikeResponse> apiResponse = ApiResponse.<LikeResponse>builder()
                .code("like.create.success")
                .message(Translator.toLocale("like.create.success"))
                .data(likeService.LikePost(postId))
                .build();
        return ResponseEntity.ok(apiResponse);
    }

    @Operation(summary = "unlike post", description = "Api unlike post")
    @DeleteMapping("/delete/{postId}")
    public ResponseEntity<ApiResponse> unlikePost(@PathVariable("postId")int postId){
        likeService.unlikePost(postId);
        ApiResponse apiResponse = ApiResponse.builder()
                .code("like.delete.success")
                .message(Translator.toLocale("like.delete.success")).build();
        return ResponseEntity.ok(apiResponse);
    }

    @Operation(summary = "get total likes", description = "Api get total likes by post", security = @SecurityRequirement(  name = ""))
    @GetMapping("/total-likes/{postId}")
    public ResponseEntity<ApiResponse<Integer>> getTotalLikesByPost(@PathVariable("postId")int postId){
        ApiResponse<Integer> apiResponse = ApiResponse.<Integer>builder()
                .code("like.get-total.success")
                .message(Translator.toLocale("like.get-total.success"))
                .data(likeService.getTotalLikes(postId)).build();
        return ResponseEntity.ok(apiResponse);
    }

    @Operation(summary = "get posts liked", description = "Api get posts liked by user")
    @GetMapping("/get-posts")
    public ResponseEntity<ApiResponse<PageResponse<PostInListResponse>>> getPostsLiked(@RequestParam(name = "pageNo", defaultValue = "0", required = false) int pageNo,
                                                                                             @RequestParam(name = "pageSize", defaultValue = "2",required = false) int pageSize,
                                                                                             @RequestParam(name = "sorts", required = false) List<String> sorts){
        ApiResponse<PageResponse<PostInListResponse>> apiResponse = ApiResponse.<PageResponse<PostInListResponse>>builder()
                .code("like.get-post-liked-by-user.success")
                .message(Translator.toLocale("like.get-post-liked-by-user.success"))
                .data(likeService.getPostsLiked(pageNo, pageSize, sorts)).build();
        return ResponseEntity.ok(apiResponse);
    }

    @Operation(summary = "get users likes post", description = "Api get users likes post")
    @GetMapping("/get-users-liked-post/{postId}")
    @PreAuthorize("@checkPermission.fileRole(#httpServletRequest)")
    public ResponseEntity<ApiResponse<PageResponse<String>>> getUsersLikedPost(@RequestParam(name = "pageNo", required = false, defaultValue = "0")int pageNo,
                                                                                @RequestParam(name = "pageSize", required = false, defaultValue = "2")int pageSize,
                                                                                @PathVariable("postId")int postId,
                                                                               HttpServletRequest httpServletRequest){
        ApiResponse<PageResponse<String>> apiResponse = ApiResponse.<PageResponse<String>>builder()
                .code("like.get-users-liked-post.success")
                .message(Translator.toLocale("like.get-users-liked-post.success"))
                .data(likeService.getUsersLikedPost(pageNo, pageSize, postId)).build();
        return ResponseEntity.ok(apiResponse);
    }
}
