package com.project.Book.controller;

import com.project.Book.config.Translator;
import com.project.Book.dto.request.CategoryRequest;
import com.project.Book.dto.request.SearchBookRequest;
import com.project.Book.dto.response.ApiResponse;
import com.project.Book.dto.response.BookInListResponse;
import com.project.Book.dto.response.CategoryResponse;
import com.project.Book.dto.response.PageResponse;
import com.project.Book.service.CategoryService;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/category")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class CategoryController {
    CategoryService categoryService;
    @PostMapping("/create")
    @PreAuthorize("@checkPermission.fileRole(#httpServletRequest)")
    public ResponseEntity<ApiResponse<CategoryResponse>> createCategory(@RequestBody @Valid CategoryRequest categoryRequest,
                                                                        HttpServletRequest httpServletRequest) {
        ApiResponse<CategoryResponse> apiResponse = ApiResponse.<CategoryResponse>builder()
                .code("category.create.success")
                .message(Translator.toLocale("category.create.success"))
                .data(categoryService.createCategory(categoryRequest)).build();
        return ResponseEntity.ok(apiResponse);
    }
    @PutMapping("/update/{cateId}")
    @PreAuthorize("@checkPermission.fileRole(#httpServletRequest)")
    public ResponseEntity<ApiResponse<CategoryResponse>> updateCategory(@PathVariable("cateId") int cateId,
                                                                        @RequestBody @Valid CategoryRequest categoryRequest,
                                                                        HttpServletRequest httpServletRequest) {
        ApiResponse<CategoryResponse> apiResponse = ApiResponse.<CategoryResponse>builder()
                .code("category.update.success")
                .message(Translator.toLocale("category.update.success"))
                .data(categoryService.updateCategory(cateId, categoryRequest)).build();
        return ResponseEntity.ok(apiResponse);
    }
    @DeleteMapping("/delete/{cateId}")
    @PreAuthorize("@checkPermission.fileRole(#httpServletRequest)")
    public ResponseEntity<ApiResponse> deleteCategory(@PathVariable("cateId")int cateId, HttpServletRequest httpServletRequest) {
        categoryService.deleteCategory(cateId);
        ApiResponse apiResponse = ApiResponse.builder()
                .code("category.delete.success")
                .message(Translator.toLocale("category.delete.success"))
                .build();
        return ResponseEntity.ok(apiResponse);
    }
    @GetMapping("/detail/{cateId}")
    @PreAuthorize("@checkPermission.fileRole(#httpServletRequest)")
    public ResponseEntity<ApiResponse<CategoryResponse>> getCategory(@PathVariable("cateId")int cateId, HttpServletRequest httpServletRequest) {
        ApiResponse<CategoryResponse> apiResponse = ApiResponse.<CategoryResponse>builder()
                .code("category.get.success")
                .message(Translator.toLocale("category.get.success"))
                .data(categoryService.getCategory(cateId)).build();
        return ResponseEntity.ok(apiResponse);
    }
    @GetMapping
    public ResponseEntity<ApiResponse<PageResponse<CategoryResponse>>> getCategories(@RequestParam(name ="pageNo", defaultValue = "0", required = false)int pageNo,
                                                                        @RequestParam(name = "pageSize", defaultValue = "10", required = false)int pageSize,
                                                                        @RequestParam(name = "cateName", required = false)String cateName,
                                                                        @RequestParam(name = "sorts", defaultValue = "cateId:asc", required = false)List<String> sorts) {
        ApiResponse<PageResponse<CategoryResponse>> apiResponse = ApiResponse.<PageResponse<CategoryResponse>>builder()
                .code("category.getlist.success")
                .message(Translator.toLocale("category.getlist.success"))
                .data(categoryService.getCategories(pageNo, pageSize, cateName, sorts)).build();
        return ResponseEntity.ok(apiResponse);
    }
    @GetMapping("/{cateId}/get-books")
    public ResponseEntity<ApiResponse<PageResponse<BookInListResponse>>> getBooksByCategory(@RequestParam(name ="pageNo", defaultValue = "0", required = false)int pageNo,
                                                                                  @RequestParam(name = "pageSize", defaultValue = "10", required = false)int pageSize,
                                                                                  @RequestBody(required = false) SearchBookRequest searchBookRequest,
                                                                                  @PathVariable("cateId") int cateId,
                                                                                  @RequestParam(name = "sorts", required = false)List<String> sorts){
        ApiResponse<PageResponse<BookInListResponse>> apiResponse = ApiResponse.<PageResponse<BookInListResponse>>builder()
                .code("category.getlistbooks.success")
                .message(Translator.toLocale("category.getlistbooks.success"))
                .data(categoryService.getBooksByCategory(pageNo, pageSize, searchBookRequest, cateId, sorts)).build();
        return ResponseEntity.ok(apiResponse);
    }
}
