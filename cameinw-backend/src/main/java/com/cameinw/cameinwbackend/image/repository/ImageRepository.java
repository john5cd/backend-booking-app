package com.cameinw.cameinwbackend.image.repository;

import com.cameinw.cameinwbackend.image.model.Image;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.List;

/**
 * The ImageRepository interface provides methods to interact with the database for Image entities.
 * It extends the JpaRepository interface, which provides basic CRUD operations for Image entities.
 *
 * This repository interface is annotated with @Repository to indicate that it is a Spring Data repository,
 * allowing it to be automatically discovered by Spring's component scanning and used for database operations.
 */
@Repository
public interface ImageRepository extends JpaRepository<Image, Integer>{
    /**
     * Retrieve a list of images associated with a specific place by its placeId.
     *
     * @param placeId The unique identifier of the place.
     * @return An optional list of images associated with the place, or an empty optional if none are found.
     */
    Optional<List<Image>> findByPlaceId(Integer placeId);
}
