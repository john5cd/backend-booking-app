package com.cameinw.cameinwbackend.user.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * A Data Transfer Object (DTO) representing a user.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserDTO {
    /**
     * The unique ID of the user.
     */
    Integer userId;

    /**
     * The username of the user.
     */
    String username;

    /**
     * The email address of the user.
     */
    String email;

    /**
     * The first name of the user.
     */
    String firstName;

    /**
     * The last name of the user.
     */
    String lastName;

    /**
     * The phone number of the user.
     */
    String phone;
}
