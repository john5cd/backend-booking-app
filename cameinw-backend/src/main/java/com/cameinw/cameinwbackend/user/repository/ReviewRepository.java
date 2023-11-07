package com.cameinw.cameinwbackend.user.repository;

import com.cameinw.cameinwbackend.place.model.Place;
import com.cameinw.cameinwbackend.user.model.Review;
import com.cameinw.cameinwbackend.user.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * The ReviewRepository interface provides methods to interact with the database for Review entities.
 * It extends the JpaRepository interface, which provides basic CRUD operations for Review entities.
 *
 * This repository interface is annotated with @Repository to indicate that it is a Spring Data repository,
 * allowing it to be automatically discovered by Spring's component scanning and used for database operations.
 */
@Repository
public interface ReviewRepository extends JpaRepository<Review, Integer>{
    /**
     * Retrieve a review by its unique identifier.
     *
     * @param reviewId The unique identifier of the review.
     * @return An optional containing the review if found, or an empty optional if not found.
     */
    Optional<Review> findById(Integer reviewId);

    /**
     * Retrieve a list of reviews associated with a specific user.
     *
     * @param user The user for whom the reviews are retrieved.
     * @return A list of reviews associated with the user.
     */
    List<Review> findByUser(User user);

    /**
     * Retrieve a list of reviews associated with a specific place.
     *
     * @param place The place for which the reviews are retrieved.
     * @return A list of reviews associated with the place.
     */
    List<Review> findByPlace(Place place);

    /**
     * Retrieve a list of reviews associated with a specific user and place.
     *
     * @param user The user for whom the reviews are retrieved.
     * @param place The place for which the reviews are retrieved.
     * @return A list of reviews associated with the user and place.
     */
    @Query(value = "SELECT r FROM Review r WHERE r.user = :user AND r.place = :place")
    List<Review> findByUserAndPlace(@Param("user") User user, @Param("place") Place place);

}
