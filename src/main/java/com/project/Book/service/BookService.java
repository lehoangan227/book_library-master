package com.project.Book.service;

import com.project.Book.dto.request.BookRequest;
import com.project.Book.dto.request.BookUpdateRequest;
import com.project.Book.dto.request.SearchBookRequest;
import com.project.Book.dto.response.BookInListResponse;
import com.project.Book.dto.response.BookResponse;
import com.project.Book.dto.response.PageResponse;

import java.util.List;


public interface BookService {
    BookResponse createBook(BookRequest bookRequest);
    BookResponse updateBook(int bookId, BookUpdateRequest bookUpdateRequest);
    void deleteBook(int bookId);
    BookResponse getBook(int bookId);
    PageResponse<BookInListResponse> getBooks(int pageNo, int pageSize, SearchBookRequest searchBookRequest, List<String> sorts);
}
