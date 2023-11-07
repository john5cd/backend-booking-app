package com.cameinw.cameinwbackend.authorization.service;

import com.cameinw.cameinwbackend.authorization.request.LoginRequest;
import com.cameinw.cameinwbackend.authorization.request.RegisterRequest;
import com.cameinw.cameinwbackend.authorization.response.AuthenticationResponse;
import com.cameinw.cameinwbackend.user.model.User;
import com.cameinw.cameinwbackend.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

/**
 * The AuthenticationService class provides methods for user registration and login,
 * including generating JWT tokens for authentication.
 *
 * This service class is annotated with @Service to indicate that it is a Spring service component,
 * allowing it to be automatically discovered by Spring's component scanning and dependency injection mechanism.
 *
 * The @RequiredArgsConstructor annotation is used here to automatically generate a constructor for this class
 * that initializes the final fields marked with the 'final' keyword. This constructor simplifies the injection
 * of dependencies required by this service, such as UserRepository, PasswordEncoder, jwtService, and authenticationManager.
 *
 */
@Service
@RequiredArgsConstructor
public class AuthenticationService {

    /**
     * The UserRepository responsible for database operations related to users.
     */
    private final UserRepository userRepository;

    /**
     * The PasswordEncoder for encoding and decoding user passwords.
     */
    private final PasswordEncoder passwordEncoder;

    /**
     * The JwtService for generating and handling JSON Web Tokens (JWTs).
     */
    private final JwtService jwtService;

    /**
     * The AuthenticationManager for authenticating users during login.
     */
    private final AuthenticationManager authenticationManager;

    /**
     * Registers a new user based on the provided registration request.
     *
     * @param request The registration request containing user details.
     * @return An AuthenticationResponse containing a JWT token and user ID.
     */
    public AuthenticationResponse register(RegisterRequest request) {
        request.validate();

        var user = User.builder()
                .theUserName(request.getTheUserName())
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .phoneNumber(request.getPhoneNumber())
                .role(request.getRole())
                .build();

        userRepository.save(user);
        var jwtToken = jwtService.generateToken(user);
        return AuthenticationResponse.builder()
                .token(jwtToken)
                .id(user.getId())
                .build();
    }

    /**
     * Authenticates a user based on the provided login request.
     *
     * @param request The login request containing user credentials.
     * @return An AuthenticationResponse containing a JWT token and user ID.
     * @throws Exception If authentication fails.
     */
    public AuthenticationResponse login(LoginRequest request) throws  Exception{

        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(),
                        request.getPassword()
                )
        );
        var user = userRepository.findByEmail(request.getEmail())
                .orElseThrow();
        var jwtToken = jwtService.generateToken(user);
        return AuthenticationResponse.builder()
                .token(jwtToken)
                .id(user.getId())
                .build();
    }
}
