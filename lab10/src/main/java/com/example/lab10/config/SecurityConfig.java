package com.example.lab10.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                // 1. Disable CSRF (Cross-Site Request Forgery)
                // This is required to allow POST requests from Postman without a token
                .csrf(csrf -> csrf.disable())

                // 2. Configure which pages are public and which are private
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/register", "/hello").permitAll() // Allow everyone to access these
                        .anyRequest().authenticated() // Secure everything else
                );

        return http.build();
    }
}