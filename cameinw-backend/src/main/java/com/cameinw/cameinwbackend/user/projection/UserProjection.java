package com.cameinw.cameinwbackend.user.projection;

/**
 * The UserProjection interface defines a projection for retrieving specific properties of a User entity.
 * It provides methods for obtaining the user's ID, username, first name, last name, phone number, and email.
 */
public interface UserProjection {
    /**
     * Gets the ID of the user.
     *
     * @return The user's ID.
     */
    Integer getId();

    /**
     * Gets the username of the user.
     *
     * @return The username of the user.
     */
    String getTheUserName();

    /**
     * Gets the first name of the user.
     *
     * @return The first name of the user.
     */
    String getFirstName();

    /**
     * Gets the last name of the user.
     *
     * @return The last name of the user.
     */
    String getLastName();

    /**
     * Gets the phone number of the user.
     *
     * @return The phone number of the user.
     */
    String getPhoneNumber();

    /**
     * Gets the email address of the user.
     *
     * @return The email address of the user.
     */
    String getEmail();
}
