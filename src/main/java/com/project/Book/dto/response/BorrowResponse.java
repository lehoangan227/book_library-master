package com.project.Book.dto.response;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class BorrowResponse {
    Integer borrowId;
    String username;
    List<String> bookTitles;
    LocalDate borrowDate;
    LocalDate dueDate;
    LocalDate returnDate;
    String status;
}
