package com.project.Book.dto.response;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class LikeResponse {
    Integer likeId;
    String likeAuthor;
    Integer postId;
    LocalDateTime createAt;
}
