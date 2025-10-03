package com.project.Book.controller;

import com.nimbusds.jose.JOSEException;
import com.project.Book.config.Translator;
import com.project.Book.dto.request.AuthenticationRequest;
import com.project.Book.dto.request.IntrospectRequest;
import com.project.Book.dto.request.LogoutRequest;
import com.project.Book.dto.request.RefreshTokenRequest;
import com.project.Book.dto.response.ApiResponse;
import com.project.Book.dto.response.AuthenticationResponse;
import com.project.Book.dto.response.IntrospectResponse;
import com.project.Book.service.AuthenticationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.text.ParseException;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Tag(name = "Authentication Controller")
public class    AuthenticationController {
    AuthenticationService authenticationService;

    @Operation(summary = "login", description = "Api login to get token", security = @SecurityRequirement(name = ""))
    @PostMapping("/token")
    public ResponseEntity<ApiResponse<AuthenticationResponse>> login(@RequestBody AuthenticationRequest authenticationRequest) throws JOSEException, ParseException {

        ApiResponse<AuthenticationResponse> apiResponse = ApiResponse.<AuthenticationResponse>builder()
                .code("auth.login.success")
                .message(Translator.toLocale("auth.login.success"))
                .data(authenticationService.login(authenticationRequest))
                .build();
        return ResponseEntity.ok(apiResponse);
    }

    @Operation(summary = "introspect token", description = "Api introspect token", security = @SecurityRequirement(name = ""))
    @PostMapping("/introspect")
    public ResponseEntity<ApiResponse<IntrospectResponse>> introspect(@RequestBody IntrospectRequest introspectRequest) throws ParseException, JOSEException {
        ApiResponse<IntrospectResponse> apiResponse = ApiResponse.<IntrospectResponse>builder()
                .code("token.vertify.success")
                .message(Translator.toLocale("token.vertify.success"))
                .data(authenticationService.introspect(introspectRequest)).build();
        return ResponseEntity.ok(apiResponse);
    }

    @Operation(summary = "logout", description = "Api logout", security = @SecurityRequirement(name = ""))
    @PostMapping("/logout")
    public ResponseEntity<ApiResponse> logout(@RequestBody LogoutRequest logoutRequest) throws ParseException, JOSEException {
        authenticationService.logout(logoutRequest);
        ApiResponse apiResponse = ApiResponse.builder()
                .code("auth.logout.success")
                .message(Translator.toLocale("auth.logout.success"))
                .build();
        return ResponseEntity.ok(apiResponse);
    }

    @Operation(summary = "refresh token", description = "Api refresh token", security = @SecurityRequirement(name = ""))
    @PostMapping("/refresh")
    public ResponseEntity<ApiResponse> refreshToken(@RequestBody RefreshTokenRequest request) throws ParseException, JOSEException {

        ApiResponse apiResponse = ApiResponse.builder()
                .code("auth.refresh.success")
                .message(Translator.toLocale("auth.refresh.success"))
                .data(authenticationService.refreshToken(request))
                .build();
        return ResponseEntity.ok(apiResponse);
    }

    @Operation(summary = "login admin", description = "Api login admin to get token", security = @SecurityRequirement(name = ""))
    @PostMapping("/login-admin")
    public ResponseEntity<ApiResponse<AuthenticationResponse>> loginAdmin(@RequestBody AuthenticationRequest authenticationRequest) throws JOSEException, ParseException {

        ApiResponse<AuthenticationResponse> apiResponse = ApiResponse.<AuthenticationResponse>builder()
                .code("auth.login-admin.success")
                .message(Translator.toLocale("auth.login-admin.success"))
                .data(authenticationService.loginAdmin(authenticationRequest))
                .build();
        return ResponseEntity.ok(apiResponse);
    }
}
