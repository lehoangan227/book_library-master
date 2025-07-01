package com.project.Book.repository;

import com.project.Book.dto.request.SearchCommentRequest;
import com.project.Book.entity.Comment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
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
        and (:#{#request.postId} is null or p.postId = :#{#request.username})
        and c.isDelete = false
    """)
    Page<Comment> searchComment(Pageable pageable, SearchCommentRequest request);
}
