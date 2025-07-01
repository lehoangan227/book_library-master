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
@Table(name = "permission")
public class Permission {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "per_id")
    int perId;
    @Column(name = "per_code", nullable = false)
    String perCode;
    @Column(name = "per_name")
    String perName;
    @Column(name = "per_desc")
    String perDesc;
    @Column(name = "is_delete")
    boolean isDelete;
    @ManyToMany(mappedBy = "permissions")
    List<Role> roles;
}
