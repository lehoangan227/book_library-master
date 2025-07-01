package com.project.Book.repository;

import com.project.Book.dto.request.SearchBookRequest;
import com.project.Book.entity.Book;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BookRepository extends JpaRepository<Book, Integer> {
    boolean existsByBookCodeAndIsDeleteFalse(String bookCode);
    Optional<Book> findByBookIdAndIsDeleteFalse(int bookId);
    boolean existsByBookIdAndIsDeleteFalse(int bookId);
    List<Book> findAllByIsDeleteFalse();
    @Query("""
        UPDATE Book b SET b.isDelete = true WHERE b.bookId = :bookId
    """)
    @Modifying
    void softDeleteByBookId(@Param("bookId") int bookId);

    @Query("""
        SELECT b FROM Book b
        WHERE (:#{#request.bookTitle} IS NULL OR b.bookTitle LIKE CONCAT('%', :#{#request.bookTitle}, '%'))
          AND (:#{#request.publisher} IS NULL OR b.publisher LIKE CONCAT('%', :#{#request.publisher}, '%'))
          AND (:#{#request.language} IS NULL OR b.language LIKE CONCAT('%', :#{#request.language}, '%'))
          AND (:#{#request.authors} IS NULL OR b.authors LIKE CONCAT('%', :#{#request.authors}, '%'))
          AND b.isDelete = false
    """)
    Page<Book> searchBooks(Pageable pageable, @Param("request") SearchBookRequest searchBookRequest);

    @Query(value = """
        select exists(select 1 from book b where b.book_id = :bookId and b.quantity_available > 0 and is_delete = false)
    """, nativeQuery = true)
    int checkQuantityAvailable(@Param("bookId")int bookId);

    @Modifying
    @Query(value = """
        update book set quantity_available = quantity_available - 1 where book_id = :bookId
    """, nativeQuery = true)
    void updateQuantityAfterBorrowing(@Param("bookId")int bookId);

    @Modifying
    @Query(value = """
        update book b set quantity_available = quantity_available + 1 where book_id = :bookId
    """, nativeQuery = true)
    void updateQuantityAfterReturning(@Param("bookId")int bookId);
}
