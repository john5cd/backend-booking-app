package com.cameinw.cameinwbackend.user.repository;

import com.cameinw.cameinwbackend.user.model.User;
import com.cameinw.cameinwbackend.user.projection.UserProjection;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;


import java.util.Optional;

/**
 * The UserRepository interface provides methods to interact with the database for User entities.
 * It extends the JpaRepository interface, which provides basic CRUD operations for User entities.
 *
 * This repository interface is annotated with @Repository to indicate that it is a Spring Data repository,
 * allowing it to be automatically discovered by Spring's component scanning and used for database operations.
 */
@Repository
public interface UserRepository extends JpaRepository<User, Integer> {
    /**
     * Retrieve a user by their unique identifier.
     *
     * @param id The unique identifier of the user.
     * @return An optional containing the user if found, or an empty optional if not found.
     */
    Optional<User> findById(Integer id);

    /**
     * Retrieve a user by their username.
     *
     * @param theUserName The username of the user.
     * @return An optional containing the user if found, or an empty optional if not found.
     */
    Optional<User> findByTheUserName(String theUserName);

    /**
     * Retrieve a user by their email address.
     *
     * @param email The email address of the user.
     * @return An optional containing the user if found, or an empty optional if not found.
     */
    Optional<User> findByEmail(String email);


    /**
     * Retrieve specific information about a user by their unique identifier using a native SQL query.
     *
     * @param id The unique identifier of the user.
     * @return A UserProjection object containing specific user information.
     */
    @Query(value ="SELECT a.id, a.theusername, a.firstname, a.lastname, a.email, a.phonenumber " +
            "FROM _user a " +
            "WHERE a.id = ?1 ", nativeQuery = true)
    UserProjection findUserById(Integer id);
}