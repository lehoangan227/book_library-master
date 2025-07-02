package com.project.Book.repository;

import com.project.Book.dto.request.SearchBorrowRequest;
import com.project.Book.entity.Borrowing;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.Optional;

@Repository
public interface BorrowRepository extends JpaRepository<Borrowing, Integer> {
    Optional<Borrowing> findByBorrowIdAndIsDeleteFalse(int borrowId);
    @Query(value = """
        select exists(select 1 from borrowing b where b.status = :status and b.user_id = :userId and b.is_delete = false)
    """, nativeQuery = true)
    int hasOverDueBorrow(@Param("userId") int userId, @Param("status")String status);

    @Modifying
    @Query(value = """
        update borrowing b set b.status = :status where b.due_date <= :today and b.is_delete = false
    """, nativeQuery = true)
    void checkStatus(@Param("status") String status, @Param("today") Date today);

    boolean existsByBorrowIdAndIsDeleteFalse(int borrowId);

    @Modifying
    @Query(value = """
        update Borrowing b set b.isDelete = true where b.borrowId = :borrowId
    """)
    void softDeleteByBorrowId(@Param("borrowId")int borrowId);

    @Query("""
        select distinct b from Borrowing b join b.books bo join b.user u 
        where (:#{#request.username} IS NULL OR u.username LIKE CONCAT('%',:#{#request.username},'%'))
        and (:#{#request.bookTitle} is null or bo.bookTitle like concat('%',:#{#request.bookTitle},'%'))
        and (:#{#request.status} is null or b.status like concat('%',:#{#request.status},'%'))
        and (:#{#request.borrowDate} is null or b.borrowDate = :#{#request.status})
        and (:#{#request.returnDate} is null or b.returnDate = :#{#request.returnDate})
        and (:#{#request.dueDate} is null or b.dueDate = :#{#request.dueDate})
        and b.isDelete = false
    """)
    Page<Borrowing> searchBorrow(Pageable pageable, @Param("request") SearchBorrowRequest searchBorrowRequest);

    @Query("""
        select distinct b from Borrowing b join b.books bo join b.user u 
        where u.userId = :userId and b.isDelete = false
    """)
    Page<Borrowing> searchBorrowByUser(Pageable pageable, @Param("userId") int userId);

    @Query("""
        select b from Borrowing b where b.user.userId = :userId and b.borrowId = :borrowId and b.isDelete = false
    """)
    Optional<Borrowing> findByBorrowIdAndUserIdAndIsDeleteFalse(@Param("borrowId") int borrowId, @Param("userId")int userId);
}
