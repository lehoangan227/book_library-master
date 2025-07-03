package com.project.Book.repository;

import com.project.Book.entity.LikePost;
import com.project.Book.entity.Post;
import com.project.Book.entity.User;
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
        select count(*) from like_post where post_id = :postId
    """, nativeQuery = true)
    Integer countLikeByPostId(@Param("postId") int postId);

    @Query("""
        select lk.post from LikePost lk where lk.user.userId = :userId
    """)
    Page<Post> getPostsLikedByUserId(@Param("userId") int userId, Pageable pageable);

    @Query("""
        select l from LikePost l join l.user u join l.post p where u.userId = :userId and p.postId = :postId and u.isDelete = false and p.isDelete = false
    """)
    Optional<LikePost> findByUserIdAndPostId(@Param("userId") int userId, @Param("postId") int postId);

    @Query("""
        select distinct u from LikePost lk join lk.user u join lk.post p where p.postId = :postId
    """)
    Page<User> getUsersLikedPostByPostId(@Param("postId") int postId, Pageable pageable);
}
