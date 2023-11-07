package com.cameinw.cameinwbackend.authorization.configuration;

import com.cameinw.cameinwbackend.authorization.filter.JwtAuthenticationFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

/**
 * The SecurityConfig class is responsible for configuring security settings for the application,
 * including authentication and authorization rules.
 *
 * This class is annotated with:
 * - @Configuration: indicates that this class is a Spring configuration class.
 * - @EnableWebSecurity: is used to enable Spring Security's web security features for the application.
 * - @RequiredArgsConstructor: automatically generates a constructor for this class that injects the final fields declared in the class.
 */

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {
    /**
     * The JwtAuthenticationFilter for handling JWT-based authentication.
     */
    private final JwtAuthenticationFilter jwtAuthFilter;
    /**
     * The AuthenticationProvider responsible for authentication.
     */
    private final AuthenticationProvider authenticationProvider;


    /**
     * Configures the security filter chain for the application.
     *
     * @param http The HttpSecurity object to configure security settings.
     * @return A SecurityFilterChain representing the configured security filter chain.
     * @throws Exception If an exception occurs during configuration.
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf((csrf) -> csrf.disable())
                .authorizeHttpRequests((requests) -> requests
                        .requestMatchers(
                                "/api/auth/**")
                        .permitAll()
                        .anyRequest()
                        .authenticated())
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authenticationProvider(authenticationProvider)
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }
}
