package com.project.Book.dto.response;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CommentResponse {
    Integer commentId;
    String commentContent;
    String commentAuthor;
    LocalDateTime createAt;
    LocalDateTime updateAt;
    Integer postId;
}
