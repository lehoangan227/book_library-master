package com.project.Book.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserCreateRequest {
    @NotBlank(message = "error.valid.username.blank")
    @Size(min = 6, max = 15, message = "error.valid.username.size")
    @Pattern(regexp = "^[a-zA-Z0-9]+$", message = "error.valid.username.format")
    String username;
    @NotBlank(message = "error.valid.password.blank")
    @Size(min = 6, max = 15, message = "error.valid.password.size")
    @Pattern(regexp = "^[a-zA-Z0-9]+$", message = "error.valid.password.format")
    String password;
    String fullName;
    @Pattern(regexp = "^0[0-9]{9}$", message = "error.valid.phone.format")
    String phoneNumber;
    @NotBlank(message = "error.valid.email.blank")
    @Email(message = "error.valid.email.format")
    String email;
    Date dob;
    String address;
}
