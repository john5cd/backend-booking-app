package com.cameinw.cameinwbackend.user.service.implementation;

import com.cameinw.cameinwbackend.exception.ResourceNotFoundException;
import com.cameinw.cameinwbackend.place.model.Place;
import com.cameinw.cameinwbackend.place.repository.PlaceRepository;
import com.cameinw.cameinwbackend.place.projection.PlaceProjection;
import com.cameinw.cameinwbackend.user.enums.Role;
import com.cameinw.cameinwbackend.user.model.User;
import com.cameinw.cameinwbackend.user.repository.UserRepository;
import com.cameinw.cameinwbackend.user.service.UserService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * The UserServiceImpl class implements the UserService interface and provides methods for managing users
 * and handling user-related operations.
 *
 * This service class is annotated with @Service to indicate that it is a Spring service component,
 * allowing it to be automatically discovered by Spring's component scanning and dependency injection mechanism.
 *
 * The @RequiredArgsConstructor annotation is used here to automatically generate a constructor for this class
 * that initializes the final fields marked with the 'final' keyword. This constructor simplifies the injection
 * of dependencies required by this service, such as UserRepository and PlaceRepository.
 */
@RequiredArgsConstructor
@Service
public class UserServiceImpl implements UserService {
    /**
     * The UserRepository responsible for database operations related to users.
     */
    private final UserRepository userRepository;
    /**
     * The PlaceRepository responsible for database operations related to places.
     */
    private final PlaceRepository placeRepository;

    /**
     * Retrieves a list of all users.
     *
     * @return A list of User entities.
     * @throws ResourceNotFoundException if no users are found.
     */
    @Override
    public List<User> getAllUsers() {
        List<User> users = userRepository.findAll();
        checkIfUsersWereFound(users);
        return users;
    }

    /**
     * Retrieves a User entity by its unique ID.
     *
     * @param userId The ID of the user to retrieve.
     * @return The User entity if found.
     * @throws ResourceNotFoundException if the user with the given ID is not found.
     */
    @Override
    public User getUserByUserId(Integer userId) {
        return getUserById(userId);
    }

    /**
     * Updates an existing User entity with new information.
     *
     * @param userId The ID of the user to update.
     * @param updatedUser The updated User entity.
     * @return The updated User entity.
     * @throws ResourceNotFoundException if the user with the given ID is not found.
     */
    @Override
    @Transactional
    public User updateUser(Integer userId, User updatedUser) {
        User existingUser = getUserById(userId);

        if (updatedUser.getFirstName()!=null){existingUser.setFirstName(updatedUser.getFirstName());}
        if (updatedUser.getLastName()!=null){existingUser.setLastName(updatedUser.getLastName());}
        if (updatedUser.getPhoneNumber()!=null){existingUser.setPhoneNumber(updatedUser.getPhoneNumber());}
        if (updatedUser.getImageName()!=null){existingUser.setImageName(updatedUser.getImageName());}

        return userRepository.save(existingUser);
    }

    /**
     * Deletes a user by their ID.
     *
     * @param userId The ID of the user to delete.
     * @throws ResourceNotFoundException if the user with the given ID is not found.
     */
    @Override
    @Transactional
    public void deleteUser(Integer userId) {
        User user = getUserById(userId);
        // Delete places associated with the user
        placeRepository.deleteByUserId(userId);
        userRepository.delete(user);
    }

    /**
     * Retrieves a list of places associated with a user by their user ID.
     *
     * @param userId The ID of the user.
     * @return A list of PlaceProjection objects representing places associated with the user.
     */
    @Override
    public Optional<List<PlaceProjection>> getPlacesByUserId(Integer userId) {
        User user = getUserById(userId);
        return placeRepository.findPlacesByUserId(userId);
    }

    /**
     * Retrieves a User entity by its unique ID.
     *
     * @param userId The ID of the user to retrieve.
     * @return The User entity if found.
     * @throws ResourceNotFoundException if the user with the given ID is not found.
     */
    private User getUserById(Integer userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found."));
    }

    /**
     * Checks if any users were found in the list.
     *
     * @param users The list of users to check.
     * @throws ResourceNotFoundException if the list is empty.
     */
    private void checkIfUsersWereFound(List<User> users) {
        if (users.isEmpty()) {
            throw new ResourceNotFoundException("No users were found.");
        }
    }

    @Override
    public String getUserRoleByUserId(Integer userId) {
        User user = getUserById(userId);
        Role role = user.getRole();
        return role.toString();
    }

}
