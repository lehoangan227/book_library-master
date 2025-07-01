package com.project.Book.service;

import com.project.Book.dto.request.BorrowCreateRequest;
import com.project.Book.dto.request.BorrowUpdateRequest;
import com.project.Book.dto.request.SearchBorrowRequest;
import com.project.Book.dto.response.BorrowResponse;
import com.project.Book.dto.response.PageResponse;

import java.util.List;

public interface BorrowService {
    BorrowResponse createBorrow(BorrowCreateRequest borrowCreateRequest);
    BorrowResponse updateStatus(int borrowId);
    void checkStatus();
    BorrowResponse updateBorrow(int borrowId, BorrowUpdateRequest borrowUpdateRequest);
    BorrowResponse getBorrow(int borrowId);
    void deleteBorrow(int borrowId);
    PageResponse<BorrowResponse> getBorrows(int pageNo, int pageSize, List<String> sorts, SearchBorrowRequest searchBorrowRequest);
}
