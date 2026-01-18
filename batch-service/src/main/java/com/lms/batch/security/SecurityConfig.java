package com.lms.batch.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(
            HttpSecurity http,
            JwtUtil jwtUtil
    ) throws Exception {

        http
            .csrf(csrf -> csrf.disable())
            .authorizeHttpRequests(auth -> auth

                // ✅ ADMIN
                .requestMatchers("/api/admin/**").hasAuthority("ROLE_ADMIN")

                // ✅ TRAINER
                .requestMatchers("/api/trainer/**").hasAuthority("ROLE_TRAINER")

                // ✅ STUDENT
                .requestMatchers("/api/student/**").hasAuthority("ROLE_STUDENT")

                // ❌ BLOCK EVERYTHING ELSE
                .anyRequest().denyAll()
            )
            .addFilterBefore(
                new JwtAuthFilter(jwtUtil),
                UsernamePasswordAuthenticationFilter.class
            );

        return http.build();
    }
}
