package com.project.Book.service.Imp;

import com.project.Book.enums.Role;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

@Service
public class CheckPermission {
    public boolean fileRole(HttpServletRequest httpServletRequest){
        String uri = httpServletRequest.getRequestURI();
        uri = uri.replaceAll("^/|/$", "").replaceAll("/", ".");
        uri = uri.replaceAll("\\.\\d+$", "");
        Properties properties = new Properties();
        try (InputStream input = getClass().getClassLoader().getResourceAsStream("role.properties")) {
            if (input == null) {
                throw new RuntimeException("File role.properties not found in classpath");
            }
            properties.load(input);
        } catch (IOException ex) {
            throw new RuntimeException("Failed to load role properties", ex);
        }
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
