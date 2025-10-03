package com.project.Book.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CategoryUpdateRequest {
    @Pattern(regexp = "^CATE(_[A-Z]+)*$", message = "error.valid.category.code.format")
    String cateCode;
    String cateName;
    String cateDesc;
}
