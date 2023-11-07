package com.cameinw.cameinwbackend.place.repository;

import com.cameinw.cameinwbackend.place.model.Place;
import com.cameinw.cameinwbackend.place.projection.PlaceProjection;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * The PlaceRepository interface provides methods to interact with the database for Place entities.
 * It extends the JpaRepository interface, which provides basic CRUD operations for Place entities.
 *
 * This repository interface is annotated with @Repository to indicate that it is a Spring Data repository,
 * allowing it to be automatically discovered by Spring's component scanning and used for database operations.
 */
@Repository
public interface PlaceRepository extends JpaRepository<Place, Integer>{
    /**
     * Retrieve a place by its unique identifier.
     *
     * @param id The unique identifier of the place.
     * @return An optional containing the place if found, or an empty optional if not found.
     */
    Optional<Place> findById(Integer id);

    /**
     * Find places matching specific criteria using a native SQL query.
     *
     * @param country The country of the places to search for.
     * @param city The city of the places to search for.
     * @param guests The minimum number of guests the places should accommodate.
     * @return A list of PlaceProjection objects matching the criteria.
     */
    @Query(value ="SELECT a.id, a.theUserName, a.address, a.name, a.cost, a.guests " +
            "FROM places a " +
            "WHERE a.country = ?1 " +
            "AND a.city = ?2 " +
            "AND a.guests > ?3 ", nativeQuery = true)
    List<PlaceProjection> findPlacesByQuery(String country, String city, Integer guests);

    /**
     * Find places associated with a specific user by their user ID using a native SQL query.
     *
     * @param userId The unique identifier of the user.
     * @return An optional list of PlaceProjection objects associated with the user, or an empty optional if none are found.
     */
    @Query(value ="SELECT a.id, a.name, a.country, a.city, a.address, a.cost, a.guests, a.bedrooms, a.bathrooms, a.type, a.description " +
            "FROM places a " +
            "WHERE a.user_id = ?1 ", nativeQuery = true)
    Optional<List<PlaceProjection>> findPlacesByUserId(Integer userId);

    /**
     * Find places by country, city, and minimum number of guests.
     *
     * @param country The country of the places to search for.
     * @param city The city of the places to search for.
     * @param guests The minimum number of guests the places should accommodate.
     * @return A list of Place objects matching the criteria.
     */
    @Query(value ="SELECT a " +
            "FROM Place a " +
            "WHERE a.country = ?1 " +
            "AND a.city = ?2 " +
            "AND a.guests = ?3 ")
    List<Place> findPlacesByCountryAndCity(String country, String city, Integer guests);

    /**
     * Get nearby places within a specified latitude and longitude range, ordered by cost.
     *
     * @param maxLat The maximum latitude.
     * @param minLat The minimum latitude.
     * @param maxLong The maximum longitude.
     * @param minLong The minimum longitude.
     * @return A list of Place objects within the specified range.
     */
    @Query(value = "Select p FROM Place where p.latitude >= ?1 and p.latitude <= ?2 and p.longitude >= ?3 and p.longitude <= ?4 order by p.cost", nativeQuery = true)
    List<Place> getNearbyPlaces(double maxLat,
                                double minLat,
                                double maxLong,
                                double minLong);

    /**
     * Deletes places associated with a specific user by their user ID.
     *
     * @param userId The unique identifier of the user whose places should be deleted.
     */
    void deleteByUserId(Integer userId);
}
