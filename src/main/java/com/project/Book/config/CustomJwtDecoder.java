package com.project.Book.config;

import com.nimbusds.jose.JOSEException;
import com.project.Book.dto.request.IntrospectRequest;
import com.project.Book.service.AuthenticationService;
import com.project.Book.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtException;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.stereotype.Component;

import javax.crypto.spec.SecretKeySpec;
import java.text.ParseException;
import java.util.Objects;

@Component
public class CustomJwtDecoder implements JwtDecoder {
    @Value("${jwt.signer-key}")
    private String signerKey;

    @Autowired
    AuthenticationService authenticationService;

    private NimbusJwtDecoder nimbusJwtDecoder = null;
    @Autowired
    private CategoryService categoryService;

    @Override
    public Jwt decode(String token) throws JwtException {
        try{
            var response = authenticationService.introspect(IntrospectRequest.builder()
                            .token(token)
                    .build());
            if(!response.getIsValid()){
                throw new JwtException("Token invalid");
            }
        }catch (ParseException | JOSEException e) {
            throw new RuntimeException(e);
        }
        if(Objects.isNull(nimbusJwtDecoder)){
            SecretKeySpec secretKeySpec = new SecretKeySpec(signerKey.getBytes(), "HS512");
            nimbusJwtDecoder = NimbusJwtDecoder
                    .withSecretKey(secretKeySpec)
                    .macAlgorithm(MacAlgorithm.HS512)
                    .build();
        }
        return nimbusJwtDecoder.decode(token);
    }
}
