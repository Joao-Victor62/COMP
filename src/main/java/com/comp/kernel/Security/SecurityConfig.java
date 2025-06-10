package com.comp.kernel.Security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {
        @Bean
        public PasswordEncoder passwordEncoder() {
            return new BCryptPasswordEncoder();
        }
    @Bean
        public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
            http
                    .csrf().disable()               // Disable CSRF (optional, depends on your needs)
                    .authorizeHttpRequests(auth -> auth
                            .anyRequest().permitAll()   // Allow all requests without auth
                    )
                    .formLogin().disable()          // Disable the default login form
                    .httpBasic().disable();         // Disable basic auth if present

            return http.build();
        }
    }

