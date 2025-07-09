package com.project.Book.controller;

import com.project.Book.config.Translator;
import com.project.Book.dto.request.CommentRequest;
import com.project.Book.dto.request.SearchCommentRequest;
import com.project.Book.dto.response.ApiResponse;
import com.project.Book.dto.response.CommentResponse;
import com.project.Book.dto.response.PageResponse;
import com.project.Book.service.CommentService;
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
@RequestMapping("/comment")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Tag(name = "Comment Controller")
public class CommentController {
    CommentService commentService;

    @Operation(summary = "create comment", description = "Api create comment")
    @PostMapping("/create/{postId}")
    @PreAuthorize("@checkPermission.fileRole(#httpServletRequest)")
    public ResponseEntity<ApiResponse<CommentResponse>> createComment(@RequestBody CommentRequest commentRequest,
                                                                      @PathVariable("postId") int postId,
                                                                      HttpServletRequest httpServletRequest) {
        ApiResponse<CommentResponse> apiResponse = ApiResponse.<CommentResponse>builder()
                .code("comment.create.success")
                .message(Translator.toLocale("comment.create.success"))
                .data(commentService.createComment(commentRequest,postId)).build();
        return ResponseEntity.ok(apiResponse);
    }

    @Operation(summary = "update comment", description = "Api update comment")
    @PutMapping("/update/{commentId}")
    @PreAuthorize("@checkPermission.fileRole(#httpServletRequest)")
    public ResponseEntity<ApiResponse<CommentResponse>> updateComment(@PathVariable("commentId")int commentId,
                                                                      @RequestBody CommentRequest commentRequest,
                                                                      HttpServletRequest httpServletRequest) {
        ApiResponse<CommentResponse> apiResponse = ApiResponse.<CommentResponse>builder()
                .code("comment.update.success")
                .message(Translator.toLocale("comment.update.success"))
                .data(commentService.updateComment(commentId, commentRequest)).build();
        return ResponseEntity.ok(apiResponse);
    }

    @Operation(summary = "delete comment", description = "Api delete comment")
    @DeleteMapping("/delete/{commentId}")
    @PreAuthorize("@checkPermission.fileRole(#httpServletRequest)")
    public ResponseEntity<ApiResponse> deleteComment(@PathVariable("commentId")int commentId,
                                                     HttpServletRequest httpServletRequest) {
        commentService.deleteComment(commentId);
        ApiResponse apiResponse = ApiResponse.builder()
                .code("comment.delete.success")
                .message(Translator.toLocale("comment.delete.success"))
                .build();
        return ResponseEntity.ok(apiResponse);
    }

    @Operation(summary = "get detail comment", description = "Api get detail comment", security = @SecurityRequirement(name = ""))
    @GetMapping("/detail/{commentId}")
    public ResponseEntity<ApiResponse<CommentResponse>> getComment(@PathVariable("commentId")int commentId) {
        ApiResponse<CommentResponse> apiResponse = ApiResponse.<CommentResponse>builder()
                .code("comment.get.success")
                .message(Translator.toLocale("comment.get.success"))
                .data(commentService.getComment(commentId)).build();
        return ResponseEntity.ok(apiResponse);
    }

    @Operation(summary = "get list comments in post", description = "Api get list comments in post", security = @SecurityRequirement(name = ""))
    @GetMapping("/{postId}")
    public ResponseEntity<ApiResponse<PageResponse<CommentResponse>>> getComments(@RequestParam(name = "pageNo", defaultValue = "0", required = false)int pageNo,
                                                                     @RequestParam(name = "pageSize", defaultValue = "2", required = false)int pageSize,
                                                                     @RequestParam(name = "sorts", defaultValue = "commentId:asc", required = false)List<String> sorts,
                                                                      @RequestBody(required = false) SearchCommentRequest searchCommentRequest,
                                                                     @PathVariable("postId")int postId,
                                                                     HttpServletRequest httpServletRequest){
        ApiResponse<PageResponse<CommentResponse>> apiResponse = ApiResponse.<PageResponse<CommentResponse>>builder()
                .code("comment.get-list.success")
                .message(Translator.toLocale("comment.get-list.success"))
                .data(commentService.getComments(pageNo, pageSize, sorts, searchCommentRequest,postId))
                .build();
        return ResponseEntity.ok(apiResponse);
    }
}
