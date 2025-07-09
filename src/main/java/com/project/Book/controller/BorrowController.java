package com.project.Book.controller;

import com.project.Book.config.Translator;
import com.project.Book.dto.request.BorrowCreateRequest;
import com.project.Book.dto.request.BorrowUpdateRequest;
import com.project.Book.dto.request.SearchBorrowRequest;
import com.project.Book.dto.response.ApiResponse;
import com.project.Book.dto.response.BorrowResponse;
import com.project.Book.dto.response.PageResponse;
import com.project.Book.service.BorrowService;
import io.swagger.v3.oas.annotations.Operation;
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
@RequestMapping("/borrow")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Tag(name = "Borrow Controller")
public class BorrowController {
    BorrowService borrowService;

    @Operation(summary = "create borrow", description = "Api create borrow")
    @PostMapping("/create")
    public ResponseEntity<ApiResponse<BorrowResponse>> createBorrow(@RequestBody BorrowCreateRequest borrowCreateRequest){
        ApiResponse<BorrowResponse> apiResponse = ApiResponse.<BorrowResponse>builder()
                .code("borrow.create.success")
                .message(Translator.toLocale("borrow.create.success"))
                .data(borrowService.createBorrow(borrowCreateRequest)).build();
        return ResponseEntity.ok(apiResponse);
    }

    @Operation(summary = "update status", description = "Api update status of borrow")
    @PatchMapping("/update-status/{borrowId}")
    @PreAuthorize("@checkPermission.fileRole(#httpServletRequest)")
    public ResponseEntity<ApiResponse<BorrowResponse>> updateStatus(@PathVariable("borrowId") int borrowId,
                                                                    HttpServletRequest httpServletRequest){
        ApiResponse<BorrowResponse> apiResponse = ApiResponse.<BorrowResponse>builder()
                .code("borrow.update-status.success")
                .message(Translator.toLocale("borrow.update-status.success"))
                .data(borrowService.updateStatus(borrowId)).build();
        return ResponseEntity.ok(apiResponse);
    }

    @Operation(summary = "update borrow", description = "Api update borrow")
    @PutMapping("/update/{borrowId}")
    @PreAuthorize("@checkPermission.fileRole(#httpServletRequest)")
    public ResponseEntity<ApiResponse<BorrowResponse>> updateBorrow(@PathVariable("borrowId")int borrowId,
                                                                    @RequestBody BorrowUpdateRequest borrowUpdateRequest,
                                                                    HttpServletRequest httpServletRequest){
        ApiResponse<BorrowResponse> apiResponse = ApiResponse.<BorrowResponse>builder()
                .code("borrow.update.success")
                .message(Translator.toLocale("borrow.update.success"))
                .data(borrowService.updateBorrow(borrowId, borrowUpdateRequest)).build();
        return ResponseEntity.ok(apiResponse);
    }

    @Operation(summary = "get detail borrow", description = "Api get detail borrow")
    @GetMapping("/detail/{borrowId}")
    @PreAuthorize("@checkPermission.fileRole(#httpServletRequest)")
    public ResponseEntity<ApiResponse<BorrowResponse>> getBorrow(@PathVariable("borrowId") int borrowId,
                                                                 HttpServletRequest httpServletRequest){
        ApiResponse<BorrowResponse> apiResponse = ApiResponse.<BorrowResponse>builder()
                .code("borrow.get.success")
                .message(Translator.toLocale("borrow.get.success"))
                .data(borrowService.getBorrow(borrowId)).build();
        return ResponseEntity.ok(apiResponse);
    }

    @Operation(summary = "delete borrow", description = "Api delete borrow")
    @DeleteMapping("/delete/{borrowId}")
    @PreAuthorize("@checkPermission.fileRole(#httpServletRequest)")
    public ResponseEntity<ApiResponse> deleteBorrow(@PathVariable("borrowId") int borrowId,
                                                    HttpServletRequest httpServletRequest){
        borrowService.deleteBorrow(borrowId);
        ApiResponse apiResponse = ApiResponse.builder()
                .code("borrow.delete.success")
                .message(Translator.toLocale("borrow.delete.success"))
                .build();
        return ResponseEntity.ok(apiResponse);
    }

    @Operation(summary = "get list borrows", description = "Api get list borrows")
    @GetMapping
    @PreAuthorize("@checkPermission.fileRole(#httpServletRequest)")
    public ResponseEntity<ApiResponse<PageResponse<BorrowResponse>>> getBorrows(@RequestParam(name = "pageNo",defaultValue = "0",required = false)int pageNo,
                                                                                @RequestParam(name = "pageSize",defaultValue = "2",required = false)int pageSize,
                                                                                @RequestParam(name = "sorts",required = false, defaultValue = "borrowId:asc")List<String> sorts,
                                                                                @RequestBody(required = false) SearchBorrowRequest searchBorrowRequest,
                                                                                HttpServletRequest httpServletRequest){
        ApiResponse<PageResponse<BorrowResponse>> apiResponse = ApiResponse.<PageResponse<BorrowResponse>>builder()
                .code("borrow.get-list.success")
                .message(Translator.toLocale("borrow.get-list.success"))
                .data(borrowService.getBorrows(pageNo,pageSize,sorts,searchBorrowRequest)).build();
        return ResponseEntity.ok(apiResponse);
    }
}
