package com.project.Book.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;
import java.util.Date;

@Entity
@Table(name = "refresh_token")
@Data
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Builder
public class RefreshToken {
    @Id
    String id;
    @Column(name = "expire_time")
    Date expireTime;
}
