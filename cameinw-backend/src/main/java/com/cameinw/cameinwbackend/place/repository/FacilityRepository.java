package com.cameinw.cameinwbackend.place.repository;

import com.cameinw.cameinwbackend.place.model.Facility;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * The FacilityRepository interface provides methods to interact with the database for Facility entities.
 * It extends the JpaRepository interface, which provides basic CRUD operations for Facility entities.
 *
 * This repository interface is annotated with @Repository to indicate that it is a Spring Data repository,
 * allowing it to be automatically discovered by Spring's component scanning and used for database operations.
 */
@Repository
public interface FacilityRepository extends JpaRepository<Facility, Integer> {
    /**
     * Retrieve a facility by its unique identifier.
     *
     * @param facilityId The unique identifier of the facility.
     * @return An optional containing the facility if found, or an empty optional if not found.
     */
    Optional<Facility> findById(Integer facilityId);

    /**
     * Check if a facility exists for a specific place by its place ID.
     *
     * @param placeId The unique identifier of the place.
     * @return true if a facility exists for the place, false otherwise.
     */
    boolean existsByPlaceId(Integer placeId);

    /**
     * Retrieve the facility associated with a specific place by its place ID.
     *
     * @param placeId The unique identifier of the place.
     * @return The facility associated with the place, or null if not found.
     */
    Facility findByPlaceId(Integer placeId);
}
