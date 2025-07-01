package com.project.Book.dto.response;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.io.Serializable;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class PageResponse<T> implements Serializable {
    int pageNo;
    int pageSize;
    int totalPage;
    long totalElements;
    List<T> items;
}
