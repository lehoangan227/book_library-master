package com.project.Book.service.Imp;

import com.project.Book.config.BeanConfig;
import com.project.Book.enums.Role;
import jakarta.servlet.http.HttpServletRequest;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

@Service
@RequiredArgsConstructor
@FieldDefaults (level = AccessLevel.PRIVATE, makeFinal = true)
public class CheckPermission {
    BeanConfig beanConfig;
    HttpServletRequest httpServletRequest;
    public boolean fileRole(HttpServletRequest request){
        String uri = "";
        if(httpServletRequest!=null && httpServletRequest.getRequestURI()!=null){
            uri = httpServletRequest.getRequestURI();
        }
        uri = uri.replaceAll("^/|/$", "").replaceAll("/", ".");
        uri = uri.replaceAll("\\.\\d+$", "");
        Properties properties = beanConfig.getPermissionFromFile();
        String requiredPermission = properties.getProperty(uri);
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || authentication.getAuthorities() == null) {
            return false;
        }
        boolean isAdmin = authentication.getAuthorities().stream()
                .anyMatch(grantedAuthority -> grantedAuthority.getAuthority().equals(Role.GROUP_ADMIN.getRoleCode()));
        if (isAdmin) {
            return true;
        }
        return authentication.getAuthorities().stream()
                .anyMatch(auth -> auth.getAuthority().equals(requiredPermission));
    }
}
