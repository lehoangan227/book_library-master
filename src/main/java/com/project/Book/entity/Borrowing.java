package com.project.Book.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
@Table(name = "borrowing")
public class Borrowing {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "borrow_id")
    int borrowId;
    @Column(name = "create_date")
    LocalDate createDate;
    @Column(name = "borrow_date")
    LocalDate borrowDate;
    @Column(name = "due_date", nullable = false)
    LocalDate dueDate;
    @Column(name = "return_date")
    LocalDate returnDate;
    @Column(name = "status")
    String status;
    @Column(name = "is_delete")
    boolean isDelete;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    User user;
    @ManyToMany
    @JoinTable(
            name = "book_borrowing",
            joinColumns = @JoinColumn(name = "borrow_id"),
            inverseJoinColumns = @JoinColumn(name = "book_id")
    )
    List<Book> books;
}
