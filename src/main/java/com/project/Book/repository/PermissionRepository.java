package com.project.Book.repository;

import com.project.Book.entity.Permission;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Repository
public interface PermissionRepository extends JpaRepository<Permission, Integer> {
    boolean existsByPerCodeAndIsDeleteFalse(String perCode);
    Optional<Permission> findByPerIdAndIsDeleteFalse(int perId);
    boolean existsByPerIdAndIsDeleteFalse(int perId);
    List<Permission> findAllByIsDeleteFalse();
    List<Permission> findAllByPerIdInAndIsDeleteFalse(List<Integer> perIds);

    @Modifying
    @Query("""
        UPDATE Permission p SET p.isDelete = true WHERE p.perId = :perId
    """)
    void softDeleteByPerId(@Param("perId") int perId);

    @Query(value = """
        select p.* from permission p join role_permission on p.per_id = role_permission.per_id 
        join user_role on role_permission.role_id = user_role.role_id 
        where user_role.user_id = :userId and p.is_delete = false
    """, nativeQuery = true)
    Set<Permission> getPermissionsByUser(@Param("userId") int userId);

    @Query("""
        SELECT p FROM Permission p
        WHERE (:perName IS NULL OR p.perName LIKE CONCAT('%',:perName ,'%'))
          AND p.isDelete = false
    """)
    Page<Permission> searchPermissions(Pageable pageable, @Param("perName") String perName);

}
