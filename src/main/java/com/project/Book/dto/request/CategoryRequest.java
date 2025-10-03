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
public class CategoryRequest {
    @NotBlank(message = "error.valid.code.blank")
    @Pattern(regexp = "^CATE(_[A-Z]+)*$", message = "error.valid.category.code.format")
    String cateCode;
    @NotBlank(message = "error.valid.name.blank")
    String cateName;
    String cateDesc;
}
