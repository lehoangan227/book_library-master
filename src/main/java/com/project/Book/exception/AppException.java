package com.project.Book.exception;

import lombok.*;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpStatus;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class AppException extends RuntimeException{
    String code;
    String message;
    HttpStatus status;
    public AppException(String code, HttpStatus status){
        this.code = code;
        this.status = status;
    }
}
