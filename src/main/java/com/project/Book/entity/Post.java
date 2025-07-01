package com.project.Book.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
@Table(name = "post")
public class Post {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "post_id")
    int postId;
    @Column(name = "post_title", nullable = false)
    String postTitle;
    @Column(name = "post_content", nullable = false)
    String postContent;
    @Column(name = "create_at")
    LocalDateTime createAt;
    @Column(name = "update_at")
    LocalDateTime updateAt;
    @Column(name = "is_delete")
    boolean isDelete;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    User user;
    @OneToMany(mappedBy = "post")
    List<Comment> comments;
    @OneToMany(mappedBy = "post")
    List<LikePost> likes;
}
