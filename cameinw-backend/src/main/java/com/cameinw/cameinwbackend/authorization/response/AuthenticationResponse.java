package com.cameinw.cameinwbackend.authorization.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * The AuthenticationResponse class represents a response object containing authentication information.
 * It includes properties such as a security token and user ID.
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AuthenticationResponse {
    /**
     * The security token used for user authentication.
     */
    private String token;

    /**
     * The unique identifier of the user associated with the authentication.
     */
    private Integer id;
}
