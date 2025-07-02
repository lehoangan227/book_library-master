package com.project.Book.dto.request;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class BorrowUpdateRequest {
    List<Integer> bookIds;
    LocalDate borrowDate;
    LocalDate returnDate;
    LocalDate dueDate;
    String status;
}
