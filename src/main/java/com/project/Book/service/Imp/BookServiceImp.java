package com.project.Book.service.Imp;

import com.project.Book.dto.request.BookRequest;
import com.project.Book.dto.request.BookUpdateRequest;
import com.project.Book.dto.request.SearchBookRequest;
import com.project.Book.dto.response.BookInListResponse;
import com.project.Book.dto.response.BookResponse;
import com.project.Book.dto.response.PageResponse;
import com.project.Book.entity.Book;
import com.project.Book.entity.Category;
import com.project.Book.exception.AppException;
import com.project.Book.mapper.BookMapper;
import com.project.Book.repository.BookRepository;
import com.project.Book.repository.CategoryRepository;
import com.project.Book.service.BookService;
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
public class BookServiceImp implements BookService {
    BookRepository bookRepository;
    CategoryRepository categoryRepository;
    BookMapper bookMapper;

    @Override
    public BookResponse createBook(BookRequest bookRequest) {
        if (bookRepository.existsByBookCodeAndIsDeleteFalse(bookRequest.getBookCode())) {
            throw new AppException("error.code.existed", HttpStatus.CONFLICT);
        }
        Book book = bookMapper.requestDtoToEntity(bookRequest);
        List<Category> categories = new ArrayList<>();
        if (bookRequest.getCateIds() != null && !bookRequest.getCateIds().isEmpty()) {
            categories = categoryRepository.findAllByCateIdInAndIsDeleteFalse(bookRequest.getCateIds());
            if (categories.size() != bookRequest.getCateIds().size()) {
                throw new AppException("error.category.notfound", HttpStatus.NOT_FOUND);
            }
            book.setCategories(categories);
        }
        book = bookRepository.save(book);
        BookResponse bookResponse = bookMapper.entityToResponseDTO(book);
        bookResponse.setCateNames(new ArrayList<>(categories.stream().map(Category::getCateName).toList()));
        return bookResponse;
    }

    @Override
    public BookResponse updateBook(int bookId, BookUpdateRequest bookUpdateRequest) {
        Book book = bookRepository.findByBookIdAndIsDeleteFalse(bookId).orElseThrow(() -> new AppException("error.book.notfound", HttpStatus.NOT_FOUND));
        if (bookRepository.existsByBookCodeAndIsDeleteFalse(bookUpdateRequest.getBookCode()) && !book.getBookCode().equals(bookUpdateRequest.getBookCode())) {
            throw new AppException("error.code.existed", HttpStatus.CONFLICT);
        }
        List<Category> categories = book.getCategories();
        bookMapper.updateDtoToEntity(book, bookUpdateRequest);
        if (bookUpdateRequest.getCateIds() != null && !bookUpdateRequest.getCateIds().isEmpty()) {
            categories = categoryRepository.findAllByCateIdInAndIsDeleteFalse(bookUpdateRequest.getCateIds());
            if (categories.size() != bookUpdateRequest.getCateIds().size()) {
                throw new AppException("error.category.notfound", HttpStatus.NOT_FOUND);
            }
            book.setCategories(categories);
        }
        book = bookRepository.save(book);
        BookResponse bookResponse = bookMapper.entityToResponseDTO(book);
        bookResponse.setCateNames(new ArrayList<>(categories.stream().map(Category::getCateName).toList()));
        return bookResponse;
    }

    @Override
    @Transactional
    public void deleteBook(int bookId) {
        if (bookRepository.existsByBookIdAndIsDeleteFalse(bookId)) {
            bookRepository.softDeleteByBookId(bookId);
        } else {
            throw new AppException("error.book.notfound", HttpStatus.NOT_FOUND);
        }
    }

    @Override
    public BookResponse getBook(int bookId) {
        Book book = bookRepository.findByBookIdAndIsDeleteFalse(bookId).orElseThrow(() -> new AppException("error.book.notfound", HttpStatus.NOT_FOUND));
        BookResponse bookResponse = bookMapper.entityToResponseDTO(book);
        bookResponse.setCateNames(new ArrayList<>(book.getCategories().stream().map(Category::getCateName).toList()));
        return bookResponse;
    }

    @Override
    public PageResponse<BookInListResponse> getBooks(int pageNo, int pageSize, SearchBookRequest searchBookRequest, List<String> sorts) {
        Pageable pageable = PageRequest.of(pageNo, pageSize, Sort.by(UtilClass.getOrders(sorts)));
        if (searchBookRequest == null) {
            searchBookRequest = new SearchBookRequest();
        }
        Page<Book> books = bookRepository.searchBooks(pageable, searchBookRequest);
        List<BookInListResponse> bookResponses = books.getContent().stream().map(bookMapper::entityToBookInListDTO).toList();
        for (int i = 0; i < bookResponses.size(); i++) {
            Book book = books.getContent().get(i);
            BookInListResponse bookResponse = bookResponses.get(i);
            List<String> cateNames = new ArrayList<>();
            if (book.getCategories() != null && !book.getCategories().isEmpty()) {
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

    @Override
    public Integer getTotalBooks() {
        return bookRepository.getTotalBooks();
    }
}
