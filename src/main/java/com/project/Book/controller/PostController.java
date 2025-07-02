package com.project.Book.controller;

import com.project.Book.config.Translator;
import com.project.Book.dto.request.PostRequest;
import com.project.Book.dto.request.SearchPostRequest;
import com.project.Book.dto.response.ApiResponse;
import com.project.Book.dto.response.PageResponse;
import com.project.Book.dto.response.PostInListResponse;
import com.project.Book.dto.response.PostResponse;
import com.project.Book.service.PostService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/post")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class PostController {
    PostService postService;
    @PostMapping("/create")
    @PreAuthorize("@checkPermission.fileRole(#httpServletRequest)")
    public ResponseEntity<ApiResponse<PostResponse>> createPost(@RequestBody PostRequest postRequest,
                                                                HttpServletRequest httpServletRequest) {
        ApiResponse<PostResponse> apiResponse = ApiResponse.<PostResponse>builder()
                .code("post.create.success")
                .message(Translator.toLocale("post.create.success"))
                .data(postService.createPost(postRequest)).build();
        return ResponseEntity.ok(apiResponse);
    }

    @PutMapping("/update/{postId}")
    @PreAuthorize("@checkPermission.fileRole(#httpServletRequest)")
    public ResponseEntity<ApiResponse<PostResponse>> updatePost(@PathVariable("postId")int postId, @RequestBody PostRequest postRequest,
                                                                HttpServletRequest httpServletRequest) {
        ApiResponse<PostResponse> apiResponse = ApiResponse.<PostResponse>builder()
                .code("post.update.success")
                .message(Translator.toLocale("post.update.success"))
                .data(postService.updatePost(postId, postRequest)).build();
        return ResponseEntity.ok(apiResponse);
    }

    @DeleteMapping("/delete/{postId}")
    @PreAuthorize("@checkPermission.fileRole(#httpServletRequest)")
    public ResponseEntity<ApiResponse> deletePost(@PathVariable("postId")int postId, HttpServletRequest httpServletRequest) {
        postService.deletePost(postId);
        ApiResponse apiResponse = ApiResponse.builder()
                .code("post.delete.success")
                .message(Translator.toLocale("post.delete.success")).build();
        return ResponseEntity.ok(apiResponse);
    }

    @GetMapping("/detail/{postId}")
    public ResponseEntity<ApiResponse<PostResponse>> getPost(@PathVariable("postId")int postId) {
        ApiResponse<PostResponse> apiResponse = ApiResponse.<PostResponse>builder()
                .code("post.get.success")
                .message(Translator.toLocale("post.get.success"))
                .data(postService.getPost(postId)).build();
        return ResponseEntity.ok(apiResponse);
    }

    @GetMapping
    public ResponseEntity<ApiResponse<PageResponse<PostInListResponse>>> getPosts(@RequestParam(name = "pageNo", defaultValue = "0", required = false)int pageNo,
                                                                     @RequestParam(name = "pageSize", defaultValue = "2", required = false)int pageSize,
                                                                     @RequestParam(name = "sorts", required = false, defaultValue = "postId:asc") List<String> sorts,
                                                                     @RequestBody(required = false) SearchPostRequest searchPostRequest) {
        ApiResponse<PageResponse<PostInListResponse>> apiResponse = ApiResponse.<PageResponse<PostInListResponse>>builder()
                .code("post.get-list.success")
                .message(Translator.toLocale("post.get-list.success"))
                .data(postService.getPosts(pageNo, pageSize, sorts, searchPostRequest)).build();
        return ResponseEntity.ok(apiResponse);
    }
}
