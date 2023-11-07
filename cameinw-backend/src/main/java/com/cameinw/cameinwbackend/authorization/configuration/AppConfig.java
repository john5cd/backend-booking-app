package com.cameinw.cameinwbackend.authorization.configuration;

import com.cameinw.cameinwbackend.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * The AppConfig class defines the configuration for the Spring Security authentication and user details retrieval.
 */
@Configuration
@RequiredArgsConstructor
public class AppConfig {
    private final UserRepository userRepository;

    /**
     * Creates and configures a UserDetailsService bean.
     *
     * @return An instance of UserDetailsService for loading user details by username.
     */
    @Bean
    public UserDetailsService userDetailsService() {
        return username -> userRepository.findByEmail(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
    }

    /**
     * Creates and configures an AuthenticationProvider bean.
     *
     * @return An instance of AuthenticationProvider for user authentication.
     */
    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailsService());
        authProvider.setPasswordEncoder(passwordEncoder());

        return authProvider;
    }

    /**
     * Creates and configures an AuthenticationManager bean.
     *
     * @param config The AuthenticationConfiguration for obtaining the AuthenticationManager.
     * @return An instance of AuthenticationManager for authentication purposes.
     * @throws Exception If an error occurs while obtaining the AuthenticationManager.
     */
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

    /**
     * Creates and configures a PasswordEncoder bean.
     *
     * @return An instance of PasswordEncoder for encoding and decoding passwords.
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
