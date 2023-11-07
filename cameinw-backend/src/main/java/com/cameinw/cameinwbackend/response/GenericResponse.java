package com.cameinw.cameinwbackend.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * The GenericResponse class represents a generic response message.
 * It is used to return simple messages in API responses.
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class GenericResponse {
    String message;
}
