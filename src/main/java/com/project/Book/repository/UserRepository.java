package com.project.Book.repository;

import com.project.Book.dto.request.SearchUserRequest;
import com.project.Book.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;


@Repository
public interface UserRepository extends JpaRepository<User, Integer> {
    boolean existsByUsernameAndIsDeleteFalse(String username);
    boolean existsByEmailAndIsDeleteFalse(String email);
    Optional<User> findByUserIdAndIsDeleteFalse(int userId);
    Optional<User> findByUsernameAndIsDeleteFalse(String username);
    boolean existsByUserIdAndIsDeleteFalse(int userId);
    List<User> findAllByUserIdInAndIsDeleteFalse(List<Integer> userIds);

    @Query(value = """
        SELECT u FROM User u
       WHERE (:#{#request.username} IS NULL OR u.username LIKE CONCAT('%',:#{#request.username},'%'))
       AND (:#{#request.fullName} IS NULL OR u.fullName LIKE CONCAT('%',:#{#request.fullName},'%'))
       AND (:#{#request.phoneNumber} IS NULL OR u.phoneNumber LIKE CONCAT('%',:#{#request.phoneNumber},'%'))
       AND (:#{#request.email} IS NULL OR u.email LIKE CONCAT('%',:#{#request.email},'%'))
       AND (:#{#request.dob} IS NULL OR u.dob = :#{#request.dob})
       AND (:#{#request.address} IS NULL OR u.address LIKE CONCAT('%',:#{#request.address},'%'))
       AND u.isDelete = false
    """)
    Page<User> searchUser(Pageable pageable, @Param("request") SearchUserRequest searchUserRequest);

    @Modifying
    @Query("UPDATE User u SET u.isDelete = true WHERE u.userId = :userId")
    void softDeleteByUserId(@Param("userId") int userId);

    @Modifying
    @Query(value = """
        INSERT INTO user_role(user_id, role_id) 
        SELECT :userId, r.role_id 
        FROM role r 
        WHERE r.role_code = :roleCode
    """, nativeQuery = true)
    void setDefaultRole(@Param("userId") int userId, @Param("roleCode") String roleCode);

    @Query(value = """
        select count(*) from user u where u.is_delete = false;
    """, nativeQuery = true)
    Integer getTotalUsers();

}
