package com.project.Book.dto.response;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class PostInListResponse {
    Integer postId;
    String postTitle;
    String postAuthor;
}
