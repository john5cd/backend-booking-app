package com.cameinw.cameinwbackend.user.repository;

import com.cameinw.cameinwbackend.place.model.Place;
import com.cameinw.cameinwbackend.user.model.Reservation;
import com.cameinw.cameinwbackend.user.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;
import java.util.Optional;

/**
 * The ReservationRepository interface provides methods to interact with the database for Reservation entities.
 * It extends the JpaRepository interface, which provides basic CRUD operations for Reservation entities.
 *
 * This repository interface is annotated with @Repository to indicate that it is a Spring Data repository,
 * allowing it to be automatically discovered by Spring's component scanning and used for database operations.
 */
@Repository
public interface ReservationRepository extends JpaRepository<Reservation, Integer> {
    /**
     * Retrieve a list of reservations associated with a specific user.
     *
     * @param user The user for whom the reservations are retrieved.
     * @return A list of reservations associated with the user.
     */
    List<Reservation> findByUser(User user);

    /**
     * Retrieve a list of reservations associated with a specific place.
     *
     * @param place The place for which the reservations are retrieved.
     * @return A list of reservations associated with the place.
     */
    List<Reservation> findByPlace(Place place);

    /**
     * Retrieve a reservation by its unique identifier.
     *
     * @param reservationId The unique identifier of the reservation.
     * @return An optional containing the reservation if found, or an empty optional if not found.
     */
    @Override
    Optional<Reservation> findById(Integer reservationId);

    /**
     * Find reservations for a specific place that overlap with a given date range.
     *
     * @param checkIn The check-in date of the range.
     * @param checkOut The check-out date of the range.
     * @param place The place for which the reservations are retrieved.
     * @return A list of reservations that overlap with the specified date range.
     */
    @Query(value ="SELECT a " +
            "FROM Reservation a " +
            "WHERE a.place = ?3 " +
            "AND ((a.checkIn<= ?1 " +
            "AND a.checkOut >= ?1 " +
            "OR a.checkIn <= ?2 " +
            "AND a.checkOut >= ?2) " +
            "OR (a.checkIn > ?1 " +
            "AND a.checkOut < ?2)) ")
    List<Reservation> findBetweenDates(Date checkIn, Date checkOut, Place place);

    /**
     * Retrieve a list of reservations associated with a specific user and place.
     *
     * @param user The user for whom the reservations are retrieved.
     * @param place The place for which the reservations are retrieved.
     * @return A list of reservations associated with the user and place.
     */
    @Query(value = "SELECT r FROM Reservation r WHERE r.user = :user AND r.place = :place")
    List<Reservation> findByUserAndPlace(@Param("user") User user, @Param("place") Place place);
}
