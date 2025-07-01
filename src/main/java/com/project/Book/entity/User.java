package com.project.Book.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.Date;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
@Table(name = "user")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    int userId;
    @Column(name = "username", nullable = false)
    String username;
    @Column(name = "password", nullable = false)
    String password;
    @Column(name = "full_name")
    String fullName;
    @Column(name = "phone_number")
    String phoneNumber;
    @Column(name = "email", nullable = false)
    String email;
    @Column(name = "dob")
    Date dob;
    @Column(name = "address")
    String address;
    @Column(name = "is_delete")
    boolean isDelete;
    @OneToMany(mappedBy = "user")
    List<Borrowing> borrowings;
    @OneToMany(mappedBy = "user")
    List<Post> posts;
    @OneToMany(mappedBy = "user")
    List<Comment> comment;
    @OneToMany(mappedBy = "user")
    List<LikePost> likes;
    @ManyToMany(mappedBy = "users")
    List<Role> roles;
}
