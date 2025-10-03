package com.project.Book.controller;

import com.project.Book.config.Translator;
import com.project.Book.dto.response.ApiResponse;
import com.project.Book.dto.response.PostResponse;
import com.project.Book.service.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
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
@Tag(name = "Dashboard Controller")
public class DashBoardController {
    UserService userService;
    PostService postService;
    CategoryService categoryService;
    BookService bookService;
    BorrowService borrowService;

    @Operation(summary = "get top 5 posts", description = "Api get top 5 posts have most likes")
    @GetMapping("/top-5-posts")
    @PreAuthorize("@checkPermission.fileRole(#httpServletRequest)")
    public ResponseEntity<ApiResponse<List<PostResponse>>> getTop5Posts(HttpServletRequest httpServletRequest) {
        ApiResponse<List<PostResponse>> apiResponse = ApiResponse.<List<PostResponse>>builder()
                .code("dashboard.get-top-5-posts.success")
                .message(Translator.toLocale("dashboard.get-top-5-posts.success"))
                .data(postService.getTop5Posts()).build();
        return ResponseEntity.ok(apiResponse);
    }

    @Operation(summary = "statistic book quantity", description = "Api statistic book quantity by category")
    @GetMapping("/statistic-book-quantity-by-category")
    @PreAuthorize("@checkPermission.fileRole(#httpServletRequest)")
    public ResponseEntity<ApiResponse<Map<String, Long>>> statisticBookQuantityByCategory(HttpServletRequest httpServletRequest) {
        ApiResponse<Map<String,Long>> apiResponse = ApiResponse.<Map<String,Long>>builder()
                .code("dashboard.statistic-book-quantity-by-category.success")
                .message(Translator.toLocale("dashboard.statistic-book-quantity-by-category.success"))
                .data(categoryService.statisticBookQuantityByCategory()).build();
        return ResponseEntity.ok(apiResponse);
    }

    @Operation(summary = "get total user", description = "Api get total user")
    @GetMapping("/get-total-user")
    @PreAuthorize("@checkPermission.fileRole(#httpServletRequest)")
    public ResponseEntity<ApiResponse<Integer>> getTotalUser(HttpServletRequest httpServletRequest) {
        ApiResponse<Integer> apiResponse = ApiResponse.<Integer>builder()
                .code("dashboard.get-total-user.success")
                .message(Translator.toLocale("dashboard.get-total-user.success"))
                .data(userService.getTotalUsers()).build();
        return ResponseEntity.ok(apiResponse);
    }

    @Operation(summary = "get total book", description = "Api get total book")
    @GetMapping("/get-total-book")
    @PreAuthorize("@checkPermission.fileRole(#httpServletRequest)")
    public ResponseEntity<ApiResponse<Integer>> getTotalBook(HttpServletRequest httpServletRequest) {
        ApiResponse<Integer> apiResponse = ApiResponse.<Integer>builder()
                .code("dashboard.get-total-book.success")
                .message(Translator.toLocale("dashboard.get-total-book.success"))
                .data(bookService.getTotalBooks()).build();
        return ResponseEntity.ok(apiResponse);
    }

    @Operation(summary = "get total borrow book", description = "get total borrow book")
    @GetMapping("/get-total-borrow-book")
    @PreAuthorize("@checkPermission.fileRole(#httpServletRequest)")
    public ResponseEntity<ApiResponse<Integer>> getTotalBorrowBook(HttpServletRequest httpServletRequest) {
        ApiResponse<Integer> apiResponse = ApiResponse.<Integer>builder()
                .code("dashboard.get-total-borrow-book.success")
                .message(Translator.toLocale("dashboard.get-total-borrow-book.success"))
                .data(borrowService.getTotalBorrowBook()).build();
        return ResponseEntity.ok(apiResponse);
    }

    @Operation(summary = "get total return book", description = "get total return book")
    @GetMapping("/get-total-return-book")
    @PreAuthorize("@checkPermission.fileRole(#httpServletRequest)")
    public ResponseEntity<ApiResponse<Integer>> getTotalReturnBook(HttpServletRequest httpServletRequest) {
        ApiResponse<Integer> apiResponse = ApiResponse.<Integer>builder()
                .code("dashboard.get-total-return-book.success")
                .message(Translator.toLocale("dashboard.get-total-return-book.success"))
                .data(borrowService.getTotalReturnBook()).build();
        return ResponseEntity.ok(apiResponse);
    }

    @Operation(summary = "get total create book", description = "get total create book")
    @GetMapping("/get-total-create-book")
    @PreAuthorize("@checkPermission.fileRole(#httpServletRequest)")
    public ResponseEntity<ApiResponse<Integer>> getTotalCreateBook(HttpServletRequest httpServletRequest) {
        ApiResponse<Integer> apiResponse = ApiResponse.<Integer>builder()
                .code("dashboard.get-total-create-book.success")
                .message(Translator.toLocale("dashboard.get-total-create-book.success"))
                .data(borrowService.getTotalCreateBook()).build();
        return ResponseEntity.ok(apiResponse);
    }

    @Operation(summary = "get total overdue book", description = "get total overdue book")
    @GetMapping("/get-total-overdue-book")
    @PreAuthorize("@checkPermission.fileRole(#httpServletRequest)")
    public ResponseEntity<ApiResponse<Integer>> getTotalOverdueBook(HttpServletRequest httpServletRequest) {
        ApiResponse<Integer> apiResponse = ApiResponse.<Integer>builder()
                .code("dashboard.get-total-overdue-book.success")
                .message(Translator.toLocale("dashboard.get-total-overdue-book.success"))
                .data(borrowService.getTotalOverdueBook()).build();
        return ResponseEntity.ok(apiResponse);
    }
}
