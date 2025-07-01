package com.project.Book.dto.request;

import jakarta.persistence.Column;
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
public class BookRequest {
    @NotBlank(message = "error.valid.code.blank")
    @Pattern(regexp = "^BOOK(_[A-Z0-9]+)*$", message = "error.valid.book.code.format")
    String bookCode;
    @NotBlank(message = "error.valid.book.title.blank")
    String bookTitle;
    String publisher;
    Integer pageCount;
    String printType;
    String language;
    String description;
    @NotBlank(message = "error.valid.book.totalquantity.blank")
    Integer totalQuantity;
    @NotBlank(message = "error.valid.book.quantityavailable.blank")
    Integer quantityAvailable;
    String authors;
    List<Integer> cateIds;
}
