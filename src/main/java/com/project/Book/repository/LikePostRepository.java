package com.project.Book.repository;

import com.project.Book.entity.LikePost;
import com.project.Book.entity.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface LikePostRepository extends JpaRepository<LikePost, Long> {
    @Query(value = """
        select * from like_post where user_id = :userId and post_id = :postId
    """, nativeQuery = true)
    Optional<LikePost> findLikePostByUserIdAndPostId(@Param("userId") int userId, @Param("postId") int postId);

    @Query(value = """
        select count(*) from like_post where post_id = :postId
    """, nativeQuery = true)
    Integer countLikeByPostId(@Param("postId") int postId);

    @Query("""
        select lk.post from LikePost lk where lk.user.userId = :userId
    """)
    Page<Post> getPostsLikedByUserId(@Param("userId") int userId, Pageable pageable);
}
