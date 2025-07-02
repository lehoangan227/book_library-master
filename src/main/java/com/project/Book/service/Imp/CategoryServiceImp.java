package com.project.Book.service.Imp;

import com.project.Book.dto.request.CategoryRequest;
import com.project.Book.dto.request.CategoryUpdateRequest;
import com.project.Book.dto.request.SearchBookRequest;
import com.project.Book.dto.response.BookInListResponse;
import com.project.Book.dto.response.CategoryResponse;
import com.project.Book.dto.response.PageResponse;
import com.project.Book.entity.Book;
import com.project.Book.entity.Category;
import com.project.Book.exception.AppException;
import com.project.Book.mapper.BookMapper;
import com.project.Book.mapper.CategoryMapper;
import com.project.Book.repository.BookRepository;
import com.project.Book.repository.CategoryRepository;
import com.project.Book.service.CategoryService;
import com.project.Book.util.UtilClass;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;


@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class CategoryServiceImp implements CategoryService {
    CategoryRepository categoryRepository;
    CategoryMapper categoryMapper;
    BookMapper bookMapper;

    @Override
    public CategoryResponse createCategory(CategoryRequest categoryRequest) {
        if(categoryRepository.existsByCateCodeAndIsDeleteFalse(categoryRequest.getCateCode())){
            throw new AppException("error.code.existed", HttpStatus.NOT_FOUND);
        }
        return categoryMapper.entityToResponseDTO(categoryRepository.save(categoryMapper.requestDtoToEntity(categoryRequest)));
    }

    @Override
    public CategoryResponse updateCategory(int cateId, CategoryUpdateRequest categoryUpdateRequest) {
        Category category = categoryRepository.findByCateIdAndIsDeleteFalse(cateId).orElseThrow(()->new AppException("error.category.notfound",HttpStatus.NOT_FOUND));
        if(categoryRepository.existsByCateCodeAndIsDeleteFalse(categoryUpdateRequest.getCateCode())&&!category.getCateCode().equals(categoryUpdateRequest.getCateCode())){
            throw new AppException("error.code.existed", HttpStatus.NOT_FOUND);
        }
        categoryMapper.updateDtoToEntity(category, categoryUpdateRequest);
        return categoryMapper.entityToResponseDTO(categoryRepository.save(category));
    }

    @Override
    @Transactional
    public void deleteCategory(int cateId) {
        if(categoryRepository.existsByCateIdAndIsDeleteFalse(cateId)){
            categoryRepository.softDeleteByCateId(cateId);
        }
        else{
            throw new AppException("error.category.notfound", HttpStatus.NOT_FOUND);
        }
    }

    @Override
    public CategoryResponse getCategory(int cateId) {
        Category category = categoryRepository.findByCateIdAndIsDeleteFalse(cateId).orElseThrow(()->new AppException("error.category.notfound",HttpStatus.NOT_FOUND));
        return categoryMapper.entityToResponseDTO(category);
    }

    @Override
    public PageResponse<CategoryResponse> getCategories(int pageNo, int pageSize, String cateName, List<String> sorts) {
        Pageable pageable = PageRequest.of(pageNo, pageSize, Sort.by(UtilClass.getOrders(sorts)));
        Page<Category> categories = categoryRepository.searchCategories(pageable, cateName);
        return PageResponse.<CategoryResponse>builder()
                .pageNo(pageNo)
                .pageSize(pageSize)
                .totalPage(categories.getTotalPages())
                .totalElements(categories.getTotalElements())
                .items(categories.getContent().stream().map(categoryMapper::entityToResponseDTO).toList())
                .build();
    }

    @Override
    public PageResponse<BookInListResponse> getBooksByCategory(int pageNo, int pageSize, SearchBookRequest searchBookRequest, int cateId, List<String> sorts) {
        Pageable pageable = PageRequest.of(pageNo, pageSize, Sort.by(UtilClass.getOrders(sorts)));
        if(searchBookRequest == null){
            searchBookRequest = new SearchBookRequest();
        }
        Page<Book> books = categoryRepository.searchBooksByCategory(pageable, searchBookRequest, cateId);
        List<BookInListResponse> bookResponses = books.getContent().stream().map(bookMapper::entityToBookInListDTO).toList();
        for(int i = 0;i<bookResponses.size();i++){
            Book book = books.getContent().get(i);
            BookInListResponse bookResponse = bookResponses.get(i);
            List<String> cateNames = new ArrayList<>();
            if(book.getCategories()!=null&&!book.getCategories().isEmpty()){
                cateNames = book.getCategories().stream()
                        .map(Category::getCateName)
                        .toList();
            }
            bookResponse.setCateNames(cateNames);
        }
        return PageResponse.<BookInListResponse>builder()
                .pageNo(pageNo)
                .pageSize(pageSize)
                .totalPage(books.getTotalPages())
                .totalElements(books.getTotalElements())
                .items(bookResponses)
                .build();
    }
}
