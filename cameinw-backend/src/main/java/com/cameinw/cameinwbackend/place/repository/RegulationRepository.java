package com.cameinw.cameinwbackend.place.repository;

import com.cameinw.cameinwbackend.place.model.Regulation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * The RegulationRepository interface provides methods to interact with the database for Regulation entities.
 * It extends the JpaRepository interface, which provides basic CRUD operations for Regulation entities.
 *
 * This repository interface is annotated with @Repository to indicate that it is a Spring Data repository,
 * allowing it to be automatically discovered by Spring's component scanning and used for database operations.
 */
@Repository
public interface RegulationRepository extends JpaRepository<Regulation, Integer> {
    /**
     * Retrieve a regulation by its unique identifier.
     *
     * @param regulationId The unique identifier of the regulation.
     * @return An optional containing the regulation if found, or an empty optional if not found.
     */
    Optional<Regulation> findById(Integer regulationId);

    /**
     * Check if a regulation exists for a specific place by its place ID.
     *
     * @param placeId The unique identifier of the place.
     * @return true if a regulation exists for the place, false otherwise.
     */
    boolean existsByPlaceId(Integer placeId);

    /**
     * Retrieve the regulation associated with a specific place by its place ID.
     *
     * @param placeId The unique identifier of the place.
     * @return The regulation associated with the place, or null if not found.
     */
    Regulation findByPlaceId(Integer placeId);

}
