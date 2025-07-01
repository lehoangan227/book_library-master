package com.project.Book.dto.request;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class SearchBorrowRequest {
    String username;
    String bookTitle;
    String status;
    LocalDate borrowDate;
    LocalDate returnDate;
    LocalDate dueDate;
}
