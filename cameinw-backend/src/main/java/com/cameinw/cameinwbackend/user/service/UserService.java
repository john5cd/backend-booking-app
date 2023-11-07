package com.cameinw.cameinwbackend.user.service;

import com.cameinw.cameinwbackend.place.projection.PlaceProjection;
import com.cameinw.cameinwbackend.user.model.User;

import java.util.List;
import java.util.Optional;

/**
 * The UserService interface defines methods for managing users and handling user-related operations.
 * It is used for operations such as retrieving, updating, and deleting users.
 */
public interface UserService {
    /**
     * Retrieve a list of all users.
     *
     * @return A list of User objects.
     */
    List<User> getAllUsers();

    /**
     * Retrieve a user by their unique identifier.
     *
     * @param userId The unique identifier of the user.
     * @return The User object associated with the provided userId.
     */
    User getUserByUserId(Integer userId);

    /**
     * Update an existing user.
     *
     * @param userId      The unique identifier of the user to update.
     * @param updatedUser The updated User object.
     * @return The updated User object.
     */
    User updateUser(Integer userId, User updatedUser);

    /**
     * Delete a user by their unique identifier.
     *
     * @param userId The unique identifier of the user to delete.
     */
    void deleteUser(Integer userId);

    /**
     * Retrieve a list of places associated with a user by their userId.
     *
     * @param userId The unique identifier of the user.
     * @return An optional list of PlaceProjection objects representing places associated with the user,
     *         or an empty optional if none are found.
     */
    Optional<List<PlaceProjection>> getPlacesByUserId(Integer userId);

    /**
     * Retrieve the role of a user by their user ID.
     *
     * @param userId The ID of the user.
     * @return The role of the user.
     * @throws ResourceNotFoundException if the user is not found.
     */
    String getUserRoleByUserId(Integer userId);

}
