package com.project.Book.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
@Table(name = "like_post")
public class LikePost {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "like_id")
    int likeId;
    @Column(name = "create_at")
    LocalDateTime createAt;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    User user;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id", nullable = false)
    Post post;
}
