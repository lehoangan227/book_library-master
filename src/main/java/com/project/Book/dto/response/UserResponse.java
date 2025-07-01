package com.project.Book.dto.response;

import jakarta.persistence.Column;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserResponse {
    int userId;
    String username;
    String password;
    String fullName;
    String phoneNumber;
    String email;
    Date dob;
    String address;
}
