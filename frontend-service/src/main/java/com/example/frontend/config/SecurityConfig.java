package com.example.frontend.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(
                                "/", "/index", "/email-page", "/edit-item", "/oops",
                                "/favicon.ico", "/static/**", "/css/**", "/js/**", "/images/**"
                        ).permitAll()
                        .anyRequest().permitAll()
                );
        return http.build();
    }
}
