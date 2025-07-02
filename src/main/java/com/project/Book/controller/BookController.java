package com.project.Book.controller;

import com.project.Book.config.Translator;
import com.project.Book.dto.request.BookRequest;
import com.project.Book.dto.request.BookUpdateRequest;
import com.project.Book.dto.request.SearchBookRequest;
import com.project.Book.dto.response.*;
import com.project.Book.service.BookService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/book")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class BookController {
    BookService bookService;
    @PostMapping("/create")
    @PreAuthorize("@checkPermission.fileRole(#httpServletRequest)")
    public ResponseEntity<ApiResponse<BookResponse>> createBook(@RequestBody BookRequest bookRequest, HttpServletRequest httpServletRequest) {
        ApiResponse<BookResponse> apiResponse = ApiResponse.<BookResponse>builder()
                .code("book.create.success")
                .message(Translator.toLocale("book.create.success"))
                .data(bookService.createBook(bookRequest)).build();
        return ResponseEntity.ok(apiResponse);
    }
    @PutMapping("/update/{bookId}")
    @PreAuthorize("@checkPermission.fileRole(#httpServletRequest)")
    public ResponseEntity<ApiResponse<BookResponse>> updateBook(@PathVariable("bookId") int bookId, @RequestBody BookUpdateRequest bookUpdateRequest,
                                                                HttpServletRequest httpServletRequest) {
        ApiResponse<BookResponse> apiResponse = ApiResponse.<BookResponse>builder()
                .code("book.update.success")
                .message(Translator.toLocale("book.update.success"))
                .data(bookService.updateBook(bookId, bookUpdateRequest)).build();
        return ResponseEntity.ok(apiResponse);
    }
    @DeleteMapping("/delete/{bookId}")
    @PreAuthorize("@checkPermission.fileRole(#httpServletRequest)")
    public ResponseEntity<ApiResponse> deleteBook(@PathVariable("bookId")int bookId, HttpServletRequest httpServletRequest) {
        bookService.deleteBook(bookId);
        ApiResponse apiResponse = ApiResponse.builder()
                .code("book.delete.success")
                .message(Translator.toLocale("book.delete.success"))
                .build();
        return ResponseEntity.ok(apiResponse);
    }
    @GetMapping("/detail/{bookId}")
    public ResponseEntity<ApiResponse<BookResponse>> getBook(@PathVariable("bookId")int bookId, HttpServletRequest httpServletRequest) {
        ApiResponse<BookResponse> apiResponse = ApiResponse.<BookResponse>builder()
                .code("book.get.success")
                .message(Translator.toLocale("book.get.success"))
                .data(bookService.getBook(bookId)).build();
        return ResponseEntity.ok(apiResponse);
    }
    @GetMapping
    public ResponseEntity<ApiResponse<PageResponse<BookInListResponse>>> getBooks(@RequestParam(name ="pageNo", defaultValue = "0", required = false)int pageNo,
                                                                                  @RequestParam(name = "pageSize", defaultValue = "10", required = false)int pageSize,
                                                                                  @RequestBody(required = false)SearchBookRequest searchBookRequest,
                                                                                  @RequestParam(name = "sorts", defaultValue = "bookId:asc", required = false) List<String> sorts){
        ApiResponse<PageResponse<BookInListResponse>> apiResponse = ApiResponse.<PageResponse<BookInListResponse>>builder()
                .code("book.getlist.success")
                .message(Translator.toLocale("book.getlist.success"))
                .data(bookService.getBooks(pageNo, pageSize, searchBookRequest, sorts)).build();
        return ResponseEntity.ok(apiResponse);
    }
}
