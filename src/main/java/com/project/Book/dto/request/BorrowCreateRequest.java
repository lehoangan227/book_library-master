package com.project.Book.dto.request;


import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.Set;
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class BorrowCreateRequest {
    Set<Integer> bookIds;
}
