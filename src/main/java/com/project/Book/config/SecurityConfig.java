package com.project.Book.config;

import com.project.Book.repository.RoleRepository;
import com.project.Book.config.CustomJwtDecoder;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.web.SecurityFilterChain;

import java.util.*;


@Configuration
@EnableWebSecurity
@EnableMethodSecurity
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class SecurityConfig {
    String[] PUBLIC_ENDPOINT = {
            "/user/create",
            "/auth/token",
            "/auth/introspect",
            "/auth/logout",
            "/auth/refresh",
            "/book/detail/*",
            "/book",
            "/category",
            "/category/*/get-books",
            "/post/detail/*", "/post",
            "/comment/detail/*",
            "/comment/*",
            "/like/total-likes/*",
            "/swagger-ui/**",
            "/v3/api-docs/**"};
    CustomJwtDecoder customJwtDecoder;
    RoleRepository roleRepository;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity httpSecurity) throws Exception {
        httpSecurity.authorizeHttpRequests(request->
                request.requestMatchers(PUBLIC_ENDPOINT).permitAll()
                .anyRequest().authenticated());
        httpSecurity.oauth2ResourceServer(oauth2 ->
                oauth2.jwt(jwtConfigurer -> jwtConfigurer.decoder(customJwtDecoder)
                        .jwtAuthenticationConverter(jwtAuthenticationConverter()))
                        .authenticationEntryPoint(new JWTAuthenticationEntryPoint()));
        httpSecurity.csrf(AbstractHttpConfigurer::disable);
        return httpSecurity.build();
    }

    @Bean
    JwtAuthenticationConverter jwtAuthenticationConverter(){
        JwtAuthenticationConverter jwtAuthenticationConverter = new JwtAuthenticationConverter();
        jwtAuthenticationConverter.setJwtGrantedAuthoritiesConverter(jwt -> {
            String[] roles = jwt.getClaimAsString("scope").split(" ");
            Set<GrantedAuthority> authorities = new HashSet<>();
            for(String roleCode : roles){
                authorities.add(new SimpleGrantedAuthority(roleCode));
                List<String> permissions = roleRepository.getAllPerCodeByRoleCode(roleCode);
                for(String perCode : permissions){
                    authorities.add(new SimpleGrantedAuthority(perCode));
                }
            }
            return authorities;
        });
        return jwtAuthenticationConverter;
    }

}
