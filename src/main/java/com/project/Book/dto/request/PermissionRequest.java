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
public class PermissionRequest {
    @NotBlank(message = "error.valid.code.blank")
    @Pattern(regexp = "^ROLE(_[A-Z]+)*$", message = "error.valid.permission.code.format")
    String perCode;
    String perName;
    String perDesc;
}
