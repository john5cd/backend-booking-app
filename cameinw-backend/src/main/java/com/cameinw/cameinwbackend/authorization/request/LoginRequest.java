package com.cameinw.cameinwbackend.authorization.request;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * The LoginRequest class represents a request object for user login.
 * It includes properties such as email and password for authentication.
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class LoginRequest {
    /**
     * The email address used for login authentication.
     */
    @NotBlank(message = "Email cannot be null or empty.")
    private String email;

    /**
     * The password used for login authentication.
     */
    @NotBlank(message = "Password cannot be null or empty.")
    private String password;
}
