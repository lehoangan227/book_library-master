package com.project.Book.enums;

import lombok.Getter;

@Getter
public enum Role {
    GROUP_ADMIN("GROUP_ADMIN"),
    GROUP_USER("GROUP_USER");
    private final String roleCode;
    Role(String roleCode) {this.roleCode = roleCode;}
}
