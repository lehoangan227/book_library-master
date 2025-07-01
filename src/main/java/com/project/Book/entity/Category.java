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
@Table(name = "category")
public class Category {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "cate_id")
    int cateId;
    @Column(name = "cate_code", nullable = false)
    String cateCode;
    @Column(name = "cate_name", nullable = false)
    String cateName;
    @Column(name = "cate_desc")
    String cateDesc;
    @Column(name = "is_delete")
    boolean isDelete;
    @ManyToMany(mappedBy = "categories")
    List<Book> books;
}
