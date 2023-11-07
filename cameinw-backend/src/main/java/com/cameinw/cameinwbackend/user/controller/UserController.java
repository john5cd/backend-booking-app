package com.cameinw.cameinwbackend.user.controller;

import com.cameinw.cameinwbackend.exception.ResourceNotFoundException;
import com.cameinw.cameinwbackend.place.projection.PlaceProjection;
import com.cameinw.cameinwbackend.response.GenericResponse;
import com.cameinw.cameinwbackend.user.dto.UserDTO;
import com.cameinw.cameinwbackend.user.model.User;
import com.cameinw.cameinwbackend.user.service.UserService;
import com.cameinw.cameinwbackend.utilities.MapToDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * The UserController class defines RESTful endpoints for managing and retrieving user information.
 *
 * This class is annotated with:
 * - @RestController: Marks this class as a REST controller, allowing it to handle HTTP requests.
 * - @RequestMapping("/api/users"): Specifies the base URL path for all endpoints in this controller.
 * - @RequiredArgsConstructor: automatically generates a constructor for this class that injects the final fields declared in the class.
 */
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/users")
public class UserController {

    /**
     * The UserService that provides methods for managing and retrieving user information.
     */
    private final UserService userService;

    /**
    /**
     * Retrieves a list of all users.
     *
     * @return A ResponseEntity containing the list of users if found, or an error response if not found.
     * @throws ResourceNotFoundException if no users are found.
     */
    @GetMapping() // !!CHECK OK!!
    public ResponseEntity<Object> getAllUsers() { // ---- check ok -----
        GenericResponse genericResponse = new GenericResponse();
        try {
            List<User> users = userService.getAllUsers();
            List<UserDTO> userDTOs = users.stream()
                    .map(user -> MapToDTO.mapUserToDTO(user)) // Map User to UserDTO
                    .collect(Collectors.toList());
            return new ResponseEntity<>(userDTOs, HttpStatus.OK);
        } catch (ResourceNotFoundException ex) {
            genericResponse.setMessage(ex.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(genericResponse);
        }
    }

    /**
     * Retrieves a user by their ID.
     *
     * @param userId The ID of the user to retrieve.
     * @return A ResponseEntity containing the user data if found, or an error response if not found.
     * @throws ResourceNotFoundException if the user is not found.
     */
    @GetMapping("/{user_id}")
    public ResponseEntity<Object> getUserById(@PathVariable("user_id") Integer userId) {
        GenericResponse genericResponse = new GenericResponse();
        try {
            User user = userService.getUserByUserId(userId); // Retrieve the User entity
            UserDTO userDTO = MapToDTO.mapUserToDTO(user); // Map the User entity to UserDTO
            return new ResponseEntity<>(userDTO, HttpStatus.OK);
        } catch (ResourceNotFoundException ex) {
            genericResponse.setMessage(ex.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(genericResponse);
        }
    }

    /**
     * Updates an existing user.
     *
     * @param userId The ID of the user to update.
     * @param updatedUser The updated User object containing the updated user data.
     * @return A ResponseEntity with a success message or an error response if not found or an error occurs.
     * @throws ResourceNotFoundException if the user is not found.
     * @throws IllegalArgumentException if an invalid argument is provided.
     */
    @PutMapping("/{user_id}") // !!CHECK OK!!
    public ResponseEntity<Object> updateUser(@PathVariable("user_id") Integer userId, @RequestBody User updatedUser) {
        GenericResponse genericResponse = new GenericResponse();
        try {
            User updated = userService.updateUser(userId, updatedUser);
            genericResponse.setMessage("User successfully updated");
            return ResponseEntity.status(HttpStatus.NO_CONTENT).body(genericResponse);
        } catch (ResourceNotFoundException ex) {
            genericResponse.setMessage(ex.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(genericResponse);
        } catch (IllegalArgumentException ex) {
            genericResponse.setMessage(ex.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(genericResponse);
        }
    }

    /**
     * Deletes a user by their ID.
     *
     * @param userId The ID of the user to delete.
     * @return A ResponseEntity with a success message or an error response if not found.
     * @throws ResourceNotFoundException if the user is not found.
     */
    @DeleteMapping("/{user_id}") // !!CHECK OK!!
    public ResponseEntity<Object> deleteUser(@PathVariable("user_id") Integer userId) {
        GenericResponse genericResponse = new GenericResponse();
        try {
            userService.deleteUser(userId);
            genericResponse.setMessage("User deleted successfully.");
            return ResponseEntity.status(HttpStatus.NO_CONTENT).body(genericResponse);
        } catch (ResourceNotFoundException ex) {
            genericResponse.setMessage(ex.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(genericResponse);
        }
    }

    /**
     * Retrieves places associated with a user by their ID.
     *
     * @param userId The ID of the user.
     * @return A ResponseEntity containing the list of places associated with the user if found, or an error response if not found.
     * @throws ResourceNotFoundException if the user or places are not found.
     */
    @GetMapping("/{user_id}/places") // !!CHECK OK!!
    public ResponseEntity<Object> getPlacesByUserId(@PathVariable("user_id") Integer userId) {
        GenericResponse genericResponse = new GenericResponse();
        try {
            List<PlaceProjection> places = userService.getPlacesByUserId(userId).orElse(Collections.emptyList());
            return new ResponseEntity<>(places, HttpStatus.OK);
        } catch (ResourceNotFoundException ex) {
            genericResponse.setMessage(ex.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(genericResponse);
        }
    }

    /**
     * Checks the validity of a user token.
     *
     * @return A ResponseEntity with a valid token response.
     */
    @GetMapping("/validUser")
    public ResponseEntity<String> checkValidUserToken() {

        return ResponseEntity.ok("{\"token\":\"valid\"}");
    }
    /**
     * Retrieves the role of a user by their ID.
     *
     * @param userId The ID of the user.
     * @return A ResponseEntity containing the user's role if found, or an error response if not found.
     * @throws ResourceNotFoundException if the user is not found.
     */
    @GetMapping("/{user_id}/role")
    public ResponseEntity<Object> getUserRoleById(@PathVariable("user_id") Integer userId) {
        GenericResponse genericResponse = new GenericResponse();
        try {
            String role = userService.getUserRoleByUserId(userId);
            genericResponse.setMessage(role);
            return ResponseEntity.status(HttpStatus.OK).body(genericResponse);
        } catch (ResourceNotFoundException ex) {
            genericResponse.setMessage(ex.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(genericResponse);
        }
    }


}
