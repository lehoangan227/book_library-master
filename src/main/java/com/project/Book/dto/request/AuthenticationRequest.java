package com.project.Book.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class AuthenticationRequest {
    @NotBlank(message = "error.valid.username.blank")
    @Size(min = 6, max = 15, message = "error.valid.username.size")
    @Pattern(regexp = "^[a-zA-Z0-9]+$", message = "error.valid.username.format")
    String username;
    @NotBlank(message = "error.valid.password.blank")
    @Size(min = 6, max = 15, message = "error.valid.password.size")
    @Pattern(regexp = "^[a-zA-Z0-9]+$", message = "error.valid.password.format")
    String password;
}
