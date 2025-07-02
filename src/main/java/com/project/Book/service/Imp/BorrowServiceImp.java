package com.project.Book.service.Imp;

import com.project.Book.dto.request.BorrowCreateRequest;
import com.project.Book.dto.request.BorrowUpdateRequest;
import com.project.Book.dto.request.SearchBorrowRequest;
import com.project.Book.dto.response.BorrowResponse;
import com.project.Book.dto.response.PageResponse;
import com.project.Book.entity.Book;
import com.project.Book.entity.Borrowing;
import com.project.Book.entity.User;
import com.project.Book.enums.Role;
import com.project.Book.enums.Status;
import com.project.Book.exception.AppException;
import com.project.Book.mapper.BorrowMapper;
import com.project.Book.repository.BookRepository;
import com.project.Book.repository.BorrowRepository;
import com.project.Book.repository.UserRepository;
import com.project.Book.service.BorrowService;
import com.project.Book.util.UtilClass;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class BorrowServiceImp implements BorrowService {
    BorrowRepository borrowRepository;
    UserRepository userRepository;
    BookRepository bookRepository;
    BorrowMapper borrowMapper;
    @Override
    @Transactional
    public BorrowResponse createBorrow(BorrowCreateRequest borrowCreateRequest) {
        Borrowing borrow = borrowMapper.createDtoToEntity(borrowCreateRequest);
        int userId = Integer.parseInt(UtilClass.getUserId());
        User user = userRepository.findByUserIdAndIsDeleteFalse(userId)
                .orElseThrow(()->new AppException("error.user.notfound", HttpStatus.NOT_FOUND));
        borrow.setUser(user);
        if(borrowCreateRequest.getBookIds().size()>3){
            throw new AppException("error.borrow-book-quantity.not-valid", HttpStatus.CONFLICT);
        }
        List<Book> books = new ArrayList<>();
        List<String>titles = new ArrayList<>();
        for(Integer bookId : borrowCreateRequest.getBookIds()){
            if(bookRepository.checkQuantityAvailable(bookId)!=1){
                throw new AppException("error.borrow-book-quantity.not-available", HttpStatus.CONFLICT);
            }
            Book book = bookRepository.findByBookIdAndIsDeleteFalse(bookId)
                    .orElseThrow(()->new AppException("error.book.notfound", HttpStatus.NOT_FOUND));
            books.add(book);
            bookRepository.updateQuantityAfterBorrowing(bookId);
            titles.add(book.getBookTitle());
        }
        borrow.setBooks(books);
        if(borrowRepository.hasOverDueBorrow(userId, Status.OVERDUE.getStatus())==1){
            throw new AppException("error.borrow.overdue",HttpStatus.CONFLICT);
        }
        borrow.setStatus(Status.CREATED.getStatus());
        borrow = borrowRepository.save(borrow);
        BorrowResponse borrowResponse = borrowMapper.entityToResponseDTO(borrow);
        borrowResponse.setUsername(user.getUsername());
        borrowResponse.setBookTitles(titles);
        return borrowResponse;
    }

    @Override
    @Transactional
    public BorrowResponse updateStatus(int borrowId) {
        Borrowing borrow = borrowRepository.findByBorrowIdAndIsDeleteFalse(borrowId)
                .orElseThrow(() -> new AppException("error.borrow.notfound", HttpStatus.NOT_FOUND));
        if (borrow.getStatus().equalsIgnoreCase(Status.CREATED.getStatus())) {
            borrow.setStatus(Status.BORROWED.getStatus());
            borrow.setBorrowDate(LocalDate.now());
            borrow.setDueDate(LocalDate.now().plusDays(14));
            borrow = borrowRepository.save(borrow);
        }
        else if (borrow.getStatus().equalsIgnoreCase(Status.BORROWED.getStatus()) || borrow.getStatus().equalsIgnoreCase(Status.OVERDUE.getStatus())) {
            borrow.setStatus(Status.RETURNED.getStatus());
            borrow.setReturnDate(LocalDate.now());
            borrow.getBooks().forEach(book -> {
                bookRepository.updateQuantityAfterReturning(book.getBookId());
            });
            borrow = borrowRepository.save(borrow);
        }
        BorrowResponse borrowResponse = borrowMapper.entityToResponseDTO(borrow);
        borrowResponse.setUsername(borrow.getUser().getUsername());
        borrowResponse.setBookTitles(borrow.getBooks().stream().map(Book::getBookTitle).toList());
        return borrowResponse;
    }

    @Override
    public PageResponse<BorrowResponse> getBorrows(int pageNo, int pageSize, List<String> sorts, SearchBorrowRequest searchBorrowRequest) {
        Pageable pageable = PageRequest.of(pageNo, pageSize, Sort.by(UtilClass.getOrders(sorts)));
        Page<Borrowing> borrowPage = null;
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        boolean isUser = auth.getAuthorities().stream()
                .anyMatch(grantedAuthority -> grantedAuthority.getAuthority().equals(Role.GROUP_USER.getRoleCode()));
        if(!isUser){
            if(searchBorrowRequest==null){
                searchBorrowRequest=new SearchBorrowRequest();
            }
            borrowPage = borrowRepository.searchBorrow(pageable, searchBorrowRequest);

        }else{
            int userId = Integer.parseInt(UtilClass.getUserId());
            borrowPage = borrowRepository.searchBorrowByUser(pageable, userId);
        }
        List<Borrowing> borrows = borrowPage.getContent();
        List<BorrowResponse> borrowResponses = borrows.stream().map(borrowMapper::entityToResponseDTO).toList();
        for (int i=0;i<borrowResponses.size();i++) {
            borrowResponses.get(i).setUsername(borrows.get(i).getUser().getUsername());
            borrowResponses.get(i).setBookTitles(borrows.get(i).getBooks().stream().map(Book::getBookTitle).toList());
        }
        return PageResponse.<BorrowResponse>builder()
                .pageNo(pageNo)
                .pageSize(pageSize)
                .totalPage(borrowPage.getTotalPages())
                .totalElements(borrowPage.getTotalElements())
                .items(borrowResponses)
                .build();
    }

    @Override
    @Transactional
    public void deleteBorrow(int borrowId) {
        if(borrowRepository.existsByBorrowIdAndIsDeleteFalse(borrowId)){
            borrowRepository.softDeleteByBorrowId(borrowId);
        }else{
            throw new AppException("error.borrow.notfound", HttpStatus.NOT_FOUND);
        }
    }

    @Override
    public BorrowResponse getBorrow(int borrowId) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        boolean isUser = auth.getAuthorities().stream()
                .anyMatch(grantedAuthority -> grantedAuthority.getAuthority().equals(Role.GROUP_USER.getRoleCode()));
        Borrowing borrow;
        if(isUser){
            int userId = Integer.parseInt(UtilClass.getUserId());
            borrow = borrowRepository.findByBorrowIdAndUserIdAndIsDeleteFalse(borrowId,userId)
                    .orElseThrow(()->new AppException("error.borrow.notfound", HttpStatus.NOT_FOUND));
        }else{
            borrow = borrowRepository.findByBorrowIdAndIsDeleteFalse(borrowId)
                    .orElseThrow(()->new AppException("error.borrow.notfound", HttpStatus.NOT_FOUND));
        }
        String username = borrow.getUser().getUsername();
        List<String> titles = borrow.getBooks().stream().map(Book::getBookTitle).toList();
        BorrowResponse borrowResponse = borrowMapper.entityToResponseDTO(borrow);
        borrowResponse.setUsername(username);
        borrowResponse.setBookTitles(titles);
        return borrowResponse;
    }

    @Override
    @Transactional
    public BorrowResponse updateBorrow(int borrowId, BorrowUpdateRequest borrowUpdateRequest) {
        Borrowing borrow = borrowRepository.findByBorrowIdAndIsDeleteFalse(borrowId)
                .orElseThrow(()->new AppException("error.borrow.notfound", HttpStatus.NOT_FOUND));
        String username = null;
        List<String>titles = new ArrayList<>();
        if(borrowUpdateRequest.getBookIds()!=null&&!borrowUpdateRequest.getBookIds().isEmpty()){
            List<Book> booksBefore = borrow.getBooks();
            for (Book book : booksBefore) {
                bookRepository.updateQuantityAfterReturning(book.getBookId());
            }
            List<Book> booksAfter = new ArrayList<>();
            for(Integer bookId : borrowUpdateRequest.getBookIds()){
                if(bookRepository.checkQuantityAvailable(bookId)!=1){
                    throw new AppException("error.borrow-book-quantity.not-available", HttpStatus.CONFLICT);
                }
                Book book = bookRepository.findByBookIdAndIsDeleteFalse(bookId)
                        .orElseThrow(()->new AppException("error.book.notfound", HttpStatus.NOT_FOUND));
                booksAfter.add(book);
                bookRepository.updateQuantityAfterBorrowing(bookId);
                titles.add(book.getBookTitle());
            }
            borrow.setBooks(booksAfter);
        }else {
            titles = borrow.getBooks().stream().map(Book::getBookTitle).toList();
        }
        if(borrowUpdateRequest.getStatus()!=null){
            borrow.setStatus(borrowUpdateRequest.getStatus());
        }
        if(borrowUpdateRequest.getBorrowDate()!=null){
            borrow.setBorrowDate(borrowUpdateRequest.getBorrowDate());
        }
        if(borrowUpdateRequest.getReturnDate()!=null){
            if(borrowUpdateRequest.getReturnDate().isBefore(borrow.getBorrowDate())||
                    borrowUpdateRequest.getReturnDate().isBefore(borrowUpdateRequest.getBorrowDate())){
                throw new AppException("error.return-date.invalid", HttpStatus.CONFLICT);
            }
            borrow.setReturnDate(borrowUpdateRequest.getReturnDate());
        }
        if(borrowUpdateRequest.getDueDate()!=null){
            if(borrowUpdateRequest.getDueDate().isBefore(borrow.getBorrowDate())||
                    borrowUpdateRequest.getDueDate().isBefore(borrowUpdateRequest.getBorrowDate())){
                throw new AppException("error.due-date.invalid", HttpStatus.CONFLICT);
            }
            borrow.setDueDate(borrowUpdateRequest.getDueDate());
        }
        borrow = borrowRepository.save(borrow);
        BorrowResponse borrowResponse = borrowMapper.entityToResponseDTO(borrow);
        borrowResponse.setUsername(username);
        borrowResponse.setBookTitles(titles);
        return borrowResponse;
    }

    @Override
    @Transactional
    @Scheduled(cron = "0 0 0 * * *")
    public void checkStatus() {
        borrowRepository.checkStatus(Status.OVERDUE.getStatus(),new Date());
    }
}
