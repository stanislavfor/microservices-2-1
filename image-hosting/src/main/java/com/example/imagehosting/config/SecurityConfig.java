package com.example.imagehosting.config;

import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

@org.springframework.context.annotation.Configuration
public class SecurityConfig {
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable())  // Отключить CSRF для REST-эндпоинтов, если потребуется
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/images/**", "/email", "/email-page", "/api/images/**").permitAll() // для image-hosting
                        .anyRequest().permitAll()  // временно всё открыто для теста
                );
        return http.build();
    }
}

