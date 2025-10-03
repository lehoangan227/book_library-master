package com.project.Book.service.Imp;

import com.nimbusds.jose.*;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jose.crypto.MACVerifier;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import com.project.Book.dto.request.AuthenticationRequest;
import com.project.Book.dto.request.IntrospectRequest;
import com.project.Book.dto.request.LogoutRequest;
import com.project.Book.dto.request.RefreshTokenRequest;
import com.project.Book.dto.response.AuthenticationResponse;
import com.project.Book.dto.response.IntrospectResponse;
import com.project.Book.entity.RefreshToken;
import com.project.Book.entity.Role;
import com.project.Book.entity.User;
import com.project.Book.exception.AppException;
import com.project.Book.repository.RefreshTokenRepository;
import com.project.Book.repository.UserRepository;
import com.project.Book.service.AuthenticationService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.experimental.NonFinal;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.sql.Timestamp;
import java.text.ParseException;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.StringJoiner;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class AuthenticationServiceImp implements AuthenticationService {
    UserRepository userRepository;
    RefreshTokenRepository refreshTokenRepository;
    @NonFinal
    @Value("${jwt.signer-key}")
    String SIGNER_KEY;

    @NonFinal
    @Value("${jwt.valid-duration}")
    long VALID_DURATION;

    @NonFinal
    @Value("${jwt.refreshable-duration}")
    long REFRESHABLE_DURATION;

    @Override
    public AuthenticationResponse login(AuthenticationRequest authenticationRequest) throws JOSEException, ParseException {
        log.info("test log");
        User user = userRepository.findByUsernameAndIsDeleteFalse(authenticationRequest.getUsername())
                .orElseThrow(()->new AppException("error.user.notfound", HttpStatus.NOT_FOUND));
        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder(10);
        boolean isAuthenticated = passwordEncoder.matches(authenticationRequest.getPassword(), user.getPassword());
        if(!isAuthenticated){
            throw new AppException("error.login.failed", HttpStatus.UNAUTHORIZED);
        }
        SignedJWT token = generateToken(user, false);
        SignedJWT refreshToken = generateToken(user,true);
        refreshTokenRepository.save(RefreshToken.builder()
                        .id(refreshToken.getJWTClaimsSet().getJWTID())
                        .expireTime(refreshToken.getJWTClaimsSet().getExpirationTime().toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime())
                .build());
        return AuthenticationResponse.builder()
                .token(token.serialize())
                .refreshToken(refreshToken.serialize())
                .build();
    }

    private SignedJWT generateToken(User user, boolean isRefresh) throws JOSEException {
        JWSHeader jwsHeader = new JWSHeader(JWSAlgorithm.HS512);
        if(isRefresh){
            JWTClaimsSet claimsSet = new JWTClaimsSet.Builder()
                    .subject(user.getUsername())
                    .issuer("library")
                    .issueTime(new Date())
                    .expirationTime(new Date(Instant.now().plus(REFRESHABLE_DURATION, ChronoUnit.SECONDS).toEpochMilli()))
                    .claim("scope", buildScope(user))
                    .claim("user_id", user.getUserId())
                    .jwtID(UUID.randomUUID().toString())
                    .build();
            SignedJWT signedJWT = new SignedJWT(jwsHeader, claimsSet);
            signedJWT.sign(new MACSigner(SIGNER_KEY.getBytes()));
            return signedJWT;
        }
        else{
            JWTClaimsSet claimsSet = new JWTClaimsSet.Builder()
                    .subject(user.getUsername())
                    .issuer("library")
                    .issueTime(new Date())
                    .expirationTime(new Date(Instant.now().plus(VALID_DURATION, ChronoUnit.SECONDS).toEpochMilli()))
                    .claim("scope", buildScope(user))
                    .claim("user_id", user.getUserId())
                    .build();
            SignedJWT signedJWT = new SignedJWT(jwsHeader, claimsSet);
            signedJWT.sign(new MACSigner(SIGNER_KEY.getBytes()));
            return signedJWT;
        }
    }

    private SignedJWT vertifyToken(String token, boolean isRefresh) throws JOSEException, ParseException {
        JWSVerifier verifier = new MACVerifier(SIGNER_KEY.getBytes());

        SignedJWT signedJWT = SignedJWT.parse(token);

        Date expireTime = signedJWT.getJWTClaimsSet().getExpirationTime();

        boolean vertified = signedJWT.verify(verifier);
        if(!(vertified&&expireTime.after(new Date()))){
            throw new AppException("error.unauthenticated", HttpStatus.UNAUTHORIZED);
        }
        if(isRefresh){
            String id = signedJWT.getJWTClaimsSet().getJWTID();
            if(!refreshTokenRepository.existsById(id))
                throw new AppException("error.unauthenticated", HttpStatus.UNAUTHORIZED);
        }
        return signedJWT;
    }

    private String buildScope(User user){
        StringJoiner stringJoiner = new StringJoiner(" ");
        if(!CollectionUtils.isEmpty(user.getRoles())){
            var roles = user.getRoles();
            var roleNames = roles.stream().map(Role::getRoleCode).toList();
            roleNames.forEach(stringJoiner::add);
        }
        return stringJoiner.toString();
    }

    @Override
    public IntrospectResponse introspect(IntrospectRequest introspectRequest) throws JOSEException, ParseException {
        String token = introspectRequest.getToken();
        boolean isValid = true;
        try{
            vertifyToken(token,false);
        }catch(AppException exception){
            isValid = false;
        }

        return IntrospectResponse.builder()
                .isValid(isValid)
                .build();
    }

    @Override
    public void logout(LogoutRequest logoutRequest) throws ParseException, JOSEException {
        SignedJWT signedJWT = vertifyToken(logoutRequest.getToken(),true);
        refreshTokenRepository.deleteById(signedJWT.getJWTClaimsSet().getJWTID());
    }

    @Override
    @Transactional
    public AuthenticationResponse refreshToken(RefreshTokenRequest request) throws ParseException, JOSEException {
        SignedJWT signedJwt = vertifyToken(request.getToken(),true);
        String id = signedJwt.getJWTClaimsSet().getJWTID();
        if(!refreshTokenRepository.existsById(id)||refreshTokenRepository.checkExpireTimeById(id, LocalDateTime.now())!=1){
            throw new AppException("error.unauthenticated", HttpStatus.UNAUTHORIZED);
        }
        refreshTokenRepository.deleteById(id);
        User user = userRepository.findByUsernameAndIsDeleteFalse(signedJwt.getJWTClaimsSet().getSubject())
                .orElseThrow(()->new AppException("error.user.notfound", HttpStatus.NOT_FOUND));
        SignedJWT token = generateToken(user, false);
        SignedJWT refreshToken = generateToken(user,true);
        refreshTokenRepository.save(RefreshToken.builder()
                .id(refreshToken.getJWTClaimsSet().getJWTID())
                .expireTime(refreshToken.getJWTClaimsSet().getExpirationTime().toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime())
                .build());
        return AuthenticationResponse.builder()
                .token(token.serialize())
                .refreshToken(refreshToken.serialize())
                .build();
    }

    @Override
    public AuthenticationResponse loginAdmin(AuthenticationRequest authenticationRequest) throws ParseException, JOSEException {
        User user = userRepository.findByUsernameAndIsDeleteFalse(authenticationRequest.getUsername())
                .orElseThrow(()->new AppException("error.user.notfound", HttpStatus.NOT_FOUND));
        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder(10);
        boolean isAuthenticated = passwordEncoder.matches(authenticationRequest.getPassword(), user.getPassword());
        SignedJWT token = generateToken(user, false);
        SignedJWT signedJWT = SignedJWT.parse(token.serialize());
        String role = signedJWT.getJWTClaimsSet().getClaim("scope").toString();
        boolean isAdmin = role.equals(com.project.Book.enums.Role.GROUP_ADMIN.toString());
        SignedJWT refreshToken = generateToken(user,true);
        if(!isAuthenticated||!isAdmin){
            throw new AppException("error.login.failed", HttpStatus.UNAUTHORIZED);
        }
        refreshTokenRepository.save(RefreshToken.builder()
                .id(refreshToken.getJWTClaimsSet().getJWTID())
                .expireTime(refreshToken.getJWTClaimsSet().getExpirationTime().toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime())
                .build());
        return AuthenticationResponse.builder()
                .token(token.serialize())
                .refreshToken(refreshToken.serialize())
                .build();
    }

    @Transactional
    @Scheduled(cron = "0 0 0 * * *")
    void cleanExpireToken(){
        log.info("Start cleaning expired refresh token at: ", System.currentTimeMillis());
        refreshTokenRepository.deleteByExpireTimeBefore(new Timestamp(System.currentTimeMillis()));
        log.info("Finish cleaning expired refresh token at: ", System.currentTimeMillis());
    }
}
