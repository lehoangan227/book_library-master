package com.project.Book.dto.request;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class SearchBookRequest {
    String bookTitle;
    String publisher;
    String language;
    String authors;
    Integer cateId;
}
