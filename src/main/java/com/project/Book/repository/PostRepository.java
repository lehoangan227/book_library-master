package com.project.Book.repository;

import com.project.Book.dto.request.SearchPostRequest;
import com.project.Book.entity.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PostRepository extends JpaRepository<Post, Long> {
    Optional<Post> findByPostIdAndIsDeleteFalse(int postId);

    @Modifying
    @Query("""
        update Post p set p.isDelete = true where p.postId = :postId
    """)
    void softDeleteByPostId(@Param("postId")int postId);

    boolean existsByPostIdAndIsDeleteFalse(int postId);

    @Query("""
        select distinct p from Post p join p.user u 
        where (:#{#request.postTitle} is null or p.postTitle like concat('%',:#{#request.postTitle},'%'))
        and (:#{#request.postAuthor} is null or u.username like concat('%',:#{#request.postAuthor},'%'))
        and p.isDelete = false
    """)
    Page<Post> searchPost(Pageable pageable, SearchPostRequest request);

    @Query(value = """
        select exists(select 1 from post p join user u on p.user_id = u.user_id 
            where p.post_id = :postId and u.user_id = :userId and p.is_delete = false and u.is_delete = false)
    """, nativeQuery = true)
    int userHasPost(@Param("postId") int postId, @Param("userId") int userId);
}
