package com.project.Book.controller;

import com.project.Book.config.Translator;
import com.project.Book.dto.response.ApiResponse;
import com.project.Book.dto.response.PostResponse;
import com.project.Book.service.CategoryService;
import com.project.Book.service.PostService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/dashboard")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class DashBoardController {
    PostService postService;
    CategoryService categoryService;
    @GetMapping("/top-5-posts")
    @PreAuthorize("@checkPermission.fileRole(#httpServletRequest)")
    public ResponseEntity<ApiResponse<List<PostResponse>>> getTop5Posts(HttpServletRequest httpServletRequest) {
        ApiResponse<List<PostResponse>> apiResponse = ApiResponse.<List<PostResponse>>builder()
                .code("dashboard.get-top-5-posts.success")
                .message(Translator.toLocale("dashboard.get-top-5-posts.success"))
                .data(postService.getTop5Posts()).build();
        return ResponseEntity.ok(apiResponse);
    }

    @GetMapping("/statistic-book-quantity-by-category")
    @PreAuthorize("@checkPermission.fileRole(#httpServletRequest)")
    public ResponseEntity<ApiResponse<Map<String, Long>>> statisticBookQuantityByCategory(HttpServletRequest httpServletRequest) {
        ApiResponse<Map<String,Long>> apiResponse = ApiResponse.<Map<String,Long>>builder()
                .code("dashboard.statistic-book-quantity-by-category.success")
                .message(Translator.toLocale("dashboard.statistic-book-quantity-by-category.success"))
                .data(categoryService.statisticBookQuantityByCategory()).build();
        return ResponseEntity.ok(apiResponse);
    }
}
