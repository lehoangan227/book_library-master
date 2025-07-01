package com.project.Book.repository;

import com.project.Book.entity.Role;
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
public interface RoleRepository extends JpaRepository<Role, Integer> {
    boolean existsByRoleCodeAndIsDeleteFalse(String roleCode);
    Optional<Role> findByRoleIdAndIsDeleteFalse(int roleId);
    boolean existsByRoleIdAndIsDeleteFalse(int roleId);
    List<Role> findAllByIsDeleteFalse();

    @Query("""
        UPDATE Role r SET r.isDelete = true WHERE r.roleId = :roleId
    """)
    @Modifying
    void softDeleteByRoleId(@Param("roleId") int roleId);

    @Query("""
        SELECT r FROM Role r
        WHERE (:roleName IS NULL OR r.roleName LIKE CONCAT('%',:roleName ,'%'))
          AND r.isDelete = false
    """)
    Page<Role> searchRoles(Pageable pageable, @Param("roleName") String roleName);


    @Query(value = """
        select per_code from permission join role_permission on permission.per_id = role_permission.per_id
        join role on role_permission.role_id = role.role_id where permission.is_delete = false and
        role.is_delete = false and role.role_code = :roleCode
    """, nativeQuery = true)
    List<String> getAllPerCodeByRoleCode(@Param("roleCode") String roleCode);
}
