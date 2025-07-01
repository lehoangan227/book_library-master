package com.project.Book.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
@Table(name = "role")
public class Role {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "role_id")
    int roleId;
    @Column(name = "role_code", nullable = false)
    String roleCode;
    @Column(name = "role_name")
    String roleName;
    @Column(name = "role_desc")
    String roleDesc;
    @Column(name = "is_delete")
    boolean isDelete;
    @ManyToMany
    @JoinTable(
            name = "user_role", joinColumns = @JoinColumn(name = "role_id"), inverseJoinColumns =@JoinColumn(name = "user_id")
    )
    List<User> users;
    @ManyToMany
    @JoinTable(
            name = "role_permission", joinColumns = @JoinColumn(name = "role_id"), inverseJoinColumns =@JoinColumn(name = "per_id")
    )
    List<Permission> permissions;
}
