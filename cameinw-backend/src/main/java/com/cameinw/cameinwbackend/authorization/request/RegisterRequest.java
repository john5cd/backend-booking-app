package com.cameinw.cameinwbackend.authorization.request;

import com.cameinw.cameinwbackend.user.enums.Role;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Arrays;
import java.util.stream.Collectors;

/**
 * The RegisterRequest class represents a request object for user registration.
 * It includes properties such as username, first name, last name, password, email, role, and phone number.
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RegisterRequest {
    /**
     * The username for registration.
     */
    @NotBlank(message = "Username cannot be null or empty.")
    private  String theUserName;

    /**
     * The first name of the user.
     */
    @NotBlank(message = "First name cannot be null or empty.")
    private String firstName;

    /**
     * The last name of the user.
     */
    @NotBlank(message = "Last name be null or empty.")
    private String lastName;

    /**
     * The password for registration.
     */
    @NotBlank(message = "Password cannot be null or empty.")
    private String password;

    /**
     * The email address for registration.
     */
    @NotBlank(message = "Email cannot be null or empty.")
    private String email;

    /**
     * The role of the user.
     */
    @NotNull(message = "Role type cannot be null.")
    private Role role;

    /**
     * The phone number of the user.
     */
    @NotBlank(message = "Phone number cannot be null or empty.")
    private String phoneNumber;

    /**
     * Validates the RegisterRequest object, ensuring that the role is valid.
     *
     * @throws IllegalArgumentException if the role is not valid.
     */
    public void validate() {
        if (!isValidRole(role)) {
            throw new IllegalArgumentException("Invalid role. Available roles are: " + getRolesList());        }
    }

    /**
     * Checks if the provided user role is valid.
     *
     * @param role The user role to check.
     * @return true if the role is valid, false otherwise.
     */
    private boolean isValidRole(Role role) {
        return Arrays.asList(Role.values()).contains(role);
    }

    /**
     * Generates a comma-separated list of available user roles.
     *
     * @return A string containing available user roles.
     */
    private String getRolesList() {
        return Arrays.stream(Role.values())
                .map(Enum::name)
                .collect(Collectors.joining(", "));
    }
}
