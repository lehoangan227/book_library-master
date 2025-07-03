package com.project.Book.service;

import com.project.Book.dto.request.CategoryRequest;
import com.project.Book.dto.request.CategoryUpdateRequest;
import com.project.Book.dto.request.SearchBookRequest;
import com.project.Book.dto.response.BookInListResponse;
import com.project.Book.dto.response.CategoryResponse;
import com.project.Book.dto.response.PageResponse;

import java.util.List;
import java.util.Map;

public interface CategoryService {
    CategoryResponse createCategory(CategoryRequest categoryRequest);
    CategoryResponse updateCategory(int cateId, CategoryUpdateRequest categoryUpdateRequest);
    void deleteCategory(int cateId);
    CategoryResponse getCategory(int cateId);
    PageResponse<CategoryResponse> getCategories(int pageNo, int pageSize, String cateName, List<String> sorts);
    PageResponse<BookInListResponse> getBooksByCategory(int pageNo, int pageSize, SearchBookRequest searchBookRequest, int cateId, List<String> sorts);
    Map<String,Integer> statisticBookQuantityByCategory();
}
