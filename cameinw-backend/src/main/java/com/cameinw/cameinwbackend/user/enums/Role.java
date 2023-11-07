package com.cameinw.cameinwbackend.user.enums;

/**
 * The Role enum represents the roles that can be assigned to users in the application.
 * Users can have either the USER role or the OWNER role.
 */
public enum Role {
    /**
     * Represents the standard USER role for regular application users.
     */
    USER,
    /**
     * Represents the OWNER role for users who have ownership or administrative privileges.
     */
    OWNER
}
