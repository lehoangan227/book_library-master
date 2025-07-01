package com.project.Book.enums;

import lombok.*;
import lombok.experimental.FieldDefaults;


@Getter
public enum Status {
    CREATED("CREATED"),
    BORROWED("BORROWED"),
    RETURNED("RETURNED"),
    OVERDUE("OVERDUE"),

    ;
    private final String status;
    Status(String status){
        this.status = status;
    }

}
