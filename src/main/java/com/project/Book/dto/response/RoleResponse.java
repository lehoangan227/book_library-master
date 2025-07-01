package com.project.Book.dto.response;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class RoleResponse {
    int roleId;
    String roleCode;
    String roleName;
    String roleDesc;
    List<String> usernames;
    List<String> perCodes;
}
