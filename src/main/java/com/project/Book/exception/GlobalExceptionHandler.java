package com.project.Book.exception;

import com.project.Book.config.Translator;
import com.project.Book.dto.response.ApiResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;



@ControllerAdvice
public class GlobalExceptionHandler {
//    @ExceptionHandler(value = Exception.class)
//    public ResponseEntity<ApiResponse> handleUnwantedException(Exception exception){
//        ApiResponse apiResponse = ApiResponse.builder()
//                .code("error.unknown")
//                .message(Translator.toLocale("error.unknown"))
//                .build();
//        return ResponseEntity.status(500).body(apiResponse);
//    }
    @ExceptionHandler(value = AppException.class)
    public ResponseEntity<ApiResponse> handleAppException(AppException exception){
        ApiResponse apiResponse = ApiResponse.builder()
                .code(exception.getCode())
                .message(Translator.toLocale(exception.getCode()))
                .build();
        return ResponseEntity.status(exception.getStatus()).body(apiResponse);
    }
    @ExceptionHandler(value = MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResponse> handleValidation(MethodArgumentNotValidException exception){
        ApiResponse apiResponse = ApiResponse.builder()
                .code(exception.getFieldError().getDefaultMessage())
                .message(Translator.toLocale(exception.getFieldError().getDefaultMessage()))
                .build();
        return ResponseEntity.badRequest().body(apiResponse);
    }
    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ApiResponse> handleAccessDeniedException(AccessDeniedException exception){
        ApiResponse apiResponse = ApiResponse.builder()
                .code("error.user.unauthorized")
                .message(Translator.toLocale("error.user.unauthorized"))
                .build();
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(apiResponse);
    }
}
