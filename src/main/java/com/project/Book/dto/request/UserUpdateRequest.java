package com.project.Book.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserUpdateRequest {
    String fullName;
    @Pattern(regexp = "^0[0-9]{9}$", message = "error.valid.phone.format")
    String phoneNumber;
    @Email(message = "error.valid.email.format")
    String email;
    Date dob;
    String address;
}
