package com.project.Book.dto.response;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class PermissionResponse {
    int perId;
    String perCode;
    String perName;
    String perDesc;
}
