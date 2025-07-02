package com.project.Book.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class BookUpdateRequest {
    @Pattern(regexp = "^BOOK(_[A-Z0-9]+)*$", message = "error.valid.book.code.format")
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
    List<Integer> cateIds;
}
