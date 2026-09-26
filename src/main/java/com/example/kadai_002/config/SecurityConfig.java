package com.example.kadai_002.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        http
            .csrf(csrf -> csrf.disable())
        .authorizeHttpRequests(auth -> auth
        		.requestMatchers("/admin/login").permitAll()
        		.requestMatchers("/admin/**").hasRole("ADMIN")
        		.requestMatchers(
        			    "/",
        			    "/shops",
        			    "/shops/**",
        			    "/signup",
        			    "/verify",
        			    "/password-reset",
        			    "/css/**",
        			    "/images/**"
        			).permitAll()
                .anyRequest().authenticated()
            )
            .formLogin(form -> form
                .loginPage("/login")
                .loginProcessingUrl("/login")
                .successHandler((request, response, authentication) -> {
                    boolean isAdmin = authentication.getAuthorities().stream()
                            .anyMatch(authority -> authority.getAuthority().equals("ROLE_ADMIN"));

                    if (isAdmin) {
                        response.sendRedirect("/admin");
                    } else {
                        response.sendRedirect("/mypage?loggedIn");
                    }
                })

                .failureUrl("/login?error")
                .permitAll()
            )
            .logout(logout -> logout
            		.logoutSuccessUrl("/?loggedOut")
                .permitAll()
            );

        return http.build();
    }
    @Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

}
