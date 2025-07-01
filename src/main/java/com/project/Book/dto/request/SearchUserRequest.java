package com.project.Book.dto.request;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.Date;
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class SearchUserRequest {
    String username;
    String fullName;
    String phoneNumber;
    String email;
    Date dob;
    String address;
}
