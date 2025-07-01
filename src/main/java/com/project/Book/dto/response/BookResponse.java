package com.project.Book.dto.response;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class BookResponse {
    Integer bookId;
    String bookCode;
    String bookTitle;
    String publisher;
    Integer pageCount;
    String printType;
    String language;
    String description;
    Integer totalQuantity;
    Integer quantityAvailable;
    String authors;
    List<String> cateNames;
}
