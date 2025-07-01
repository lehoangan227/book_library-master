package com.project.Book.service;

import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.KeyLengthException;
import com.project.Book.dto.request.AuthenticationRequest;
import com.project.Book.dto.request.IntrospectRequest;
import com.project.Book.dto.request.LogoutRequest;
import com.project.Book.dto.request.RefreshTokenRequest;
import com.project.Book.dto.response.AuthenticationResponse;
import com.project.Book.dto.response.IntrospectResponse;
import com.project.Book.entity.User;

import java.text.ParseException;

public interface AuthenticationService {
    AuthenticationResponse login(AuthenticationRequest authenticationRequest) throws JOSEException, ParseException;
    IntrospectResponse introspect(IntrospectRequest introspectRequest) throws JOSEException, ParseException;
    void logout(LogoutRequest logoutRequest) throws ParseException, JOSEException;
    AuthenticationResponse refreshToken(RefreshTokenRequest request) throws ParseException, JOSEException;
}
