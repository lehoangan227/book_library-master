package com.project.Book.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

@Configuration
public class BeanConfig {
    @Bean
    public Properties getPermissionFromFile() {
        Properties prop = new Properties();
        try (InputStream input = getClass().getClassLoader().getResourceAsStream("role.properties")) {
            if (input == null) {
                throw new RuntimeException("File role.properties not found in classpath");
            }
            prop.load(input);
        } catch (IOException ex) {
            throw new RuntimeException("Failed to load role properties", ex);
        }
        return prop;
    }
}
