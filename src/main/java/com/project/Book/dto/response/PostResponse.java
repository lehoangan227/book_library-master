package com.project.Book.dto.response;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class PostResponse {
    Integer postId;
    String postTitle;
    String postContent;
    String postAuthor;
    LocalDateTime createAt;
    LocalDateTime updateAt;
}
