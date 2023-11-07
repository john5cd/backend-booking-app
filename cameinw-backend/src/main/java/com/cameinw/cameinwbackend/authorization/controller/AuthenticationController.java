package com.cameinw.cameinwbackend.authorization.controller;

import com.cameinw.cameinwbackend.authorization.request.LoginRequest;
import com.cameinw.cameinwbackend.authorization.request.RegisterRequest;
import com.cameinw.cameinwbackend.authorization.response.AuthenticationResponse;
import com.cameinw.cameinwbackend.authorization.service.AuthenticationService;
import com.cameinw.cameinwbackend.exception.ResourceNotFoundException;
import com.cameinw.cameinwbackend.user.repository.UserRepository;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * The AuthenticationController class defines RESTful endpoints for user authentication and registration.
 *
 * This class is annotated with:
 * - @RestController: Marks this class as a REST controller, allowing it to handle HTTP requests.
 * - @RequestMapping("/api/auth"): Specifies the base URL path for all authentication-related endpoints in this controller.
 * - @RequiredArgsConstructor: automatically generates a constructor for this class that injects the final fields declared in the class.
 */
@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthenticationController {

    private final AuthenticationService authenticationService;
    /**
     * The UserRepository responsible for database operations related to users.
     */
    private final UserRepository userRepository;

    /**
     * Registers a new user with the provided registration request.
     *
     * @param registerRequest The registration request containing user information.
     * @return ResponseEntity containing the authentication response if registration is successful.
     */
    @PostMapping("/register") //!!CHECK OK!!
    public ResponseEntity<Object> register(
            @Valid  @RequestBody RegisterRequest registerRequest
    ) {
        if (userRepository.findByEmail(registerRequest.getEmail()).isEmpty() &&
                userRepository.findByTheUserName(registerRequest.getTheUserName()).isEmpty())
        {
            return ResponseEntity.ok(authenticationService.register(registerRequest));
        } else {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Username or email already exist");
        }
    }

    /**
     * Logs in a user with the provided login request.
     *
     * @param loginRequest The login request containing user credentials.
     * @return ResponseEntity containing the authentication response if login is successful.
     * @throws ResourceNotFoundException if the user does not exist.
     * @throws Exception If authentication fails.
     */
    @PostMapping("/login") //!!CHECK OK!!
    public ResponseEntity<AuthenticationResponse> login(
            @Valid @RequestBody LoginRequest loginRequest
    ) throws Exception{
        if (!userRepository.findByEmail(loginRequest.getEmail()).isPresent()) {
            throw new ResourceNotFoundException("User not found");
        }
        return ResponseEntity.ok(authenticationService.login(loginRequest));
    }
}
