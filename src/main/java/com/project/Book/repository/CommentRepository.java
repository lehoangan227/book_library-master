package com.project.Book.repository;

import com.project.Book.dto.request.SearchCommentRequest;
import com.project.Book.entity.Comment;
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
public interface CommentRepository extends JpaRepository<Comment, Integer> {
    Optional<Comment> findByCommentIdAndIsDeleteFalse(int commentId);

    boolean existsByCommentIdAndIsDeleteFalse(int commentId);

    @Modifying
    @Query("""
        update Comment c set c.isDelete = true where c.commentId = :commentId
    """)
    void softDeleteByCommentId(int commentId);

    @Query("""
        select distinct c from Comment c join c.user u join c.post p
        where (:#{#request.username} is null or u.username like concat('%',:#{#request.username}))
        and (:#{#request.postId} is null or p.postId = :#{#request.username}) and p.postId = :postId
        and c.isDelete = false
    """)
    Page<Comment> searchComment(Pageable pageable, SearchCommentRequest request, @Param("postId")int postId);
    @Query("""
        select exists (select 1 from Comment c join c.user u 
            where c.commentId = :commentId and u.userId = :userId and c.isDelete = false and u.isDelete = false)
    """)
    boolean userHasComment(@Param("commentId")int commentId, @Param("userId")int userId);

    int post(Post post);
}
