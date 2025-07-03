package com.project.Book.controller;

import com.project.Book.config.Translator;
import com.project.Book.dto.response.ApiResponse;
import com.project.Book.dto.response.PostResponse;
import com.project.Book.service.CategoryService;
import com.project.Book.service.PostService;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/dashboard")
@RequiredArgsConstructor
@FieldDefaults
public class DashBoardController {
    PostService postService;
    CategoryService categoryService;
    @GetMapping("/top-5-posts")
    public ResponseEntity<ApiResponse<List<PostResponse>>> getTop5Posts() {
        ApiResponse<List<PostResponse>> apiResponse = ApiResponse.<List<PostResponse>>builder()
                .code("dashboard.get-top-5-posts.success")
                .message(Translator.toLocale("dashboard.get-top-5-posts.success"))
                .data(postService.getTop5Posts()).build();
        return ResponseEntity.ok(apiResponse);
    }

    @GetMapping("/statistic-book-quantity-by-category")
    public ResponseEntity<ApiResponse<Map<String, Integer>>> statisticBookQuantityByCategory() {
        ApiResponse<Map<String,Integer>> apiResponse = ApiResponse.<Map<String,Integer>>builder()
                .code("dashboard.statistic-book-quantity-by-category.success")
                .message(Translator.toLocale("dashboard.statistic-book-quantity-by-category.success"))
                .data(categoryService.statisticBookQuantityByCategory()).build();
        return ResponseEntity.ok(apiResponse);
    }
}
