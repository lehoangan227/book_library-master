package com.project.Book.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
@Table(name = "book")
public class Book {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "book_id")
    int bookId;
    @Column(name = "book_code", nullable = false)
    String bookCode;
    @Column(name = "book_title", nullable = false)
    String bookTitle;
    @Column(name = "publisher")
    String publisher;
    @Column(name = "page_count")
    int pageCount;
    @Column(name = "print_type")
    String printType;
    @Column(name = "language")
    String language;
    @Column(name = "description")
    String description;
    @Column(name = "total_quantity", nullable = false)
    int totalQuantity;
    @Column(name = "quantity_available", nullable = false)
    int quantityAvailable;
    @Column(name = "is_delete")
    boolean isDelete;
    @Column(name = "authors")
    String authors;
    @ManyToMany(mappedBy = "books")
    List<Borrowing> borrows;
    @ManyToMany
    @JoinTable(
            name = "book_category",
            joinColumns = @JoinColumn(name = "book_id"),
            inverseJoinColumns = @JoinColumn(name = "cate_id")
    )
    List<Category> categories;
}
