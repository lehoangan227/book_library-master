package com.project.Book.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class RoleRequest {
    @NotBlank(message = "error.valid.code.blank")
    @Pattern(regexp = "^GROUP(_[A-Z]+)*$", message = "error.valid.role.code.format")
    String roleCode;
    String roleName;
    String roleDesc;
    List<Integer> userIds;
    List<Integer> perIds;
}
