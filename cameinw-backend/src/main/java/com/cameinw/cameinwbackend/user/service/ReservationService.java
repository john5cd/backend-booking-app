package com.cameinw.cameinwbackend.user.service;

import com.cameinw.cameinwbackend.place.model.Place;
import com.cameinw.cameinwbackend.user.model.Reservation;
import com.cameinw.cameinwbackend.user.request.ReservationRequest;

import java.util.List;

/**
 * The ReservationService interface defines methods for managing reservations related to places.
 * It is used for operations such as creating and retrieving reservations.
 */
public interface ReservationService {
    /**
     * Make a new reservation for a place.
     *
     * @param placeId           The unique identifier of the place.
     * @param reservationRequest The request object containing reservation information.
     * @return The newly created Reservation object.
     */
    Reservation makeReservation(Integer placeId, ReservationRequest reservationRequest);

    /**
     * Retrieve a list of reservations associated with a user by userId.
     *
     * @param userId The unique identifier of the user.
     * @return A list of Reservation objects associated with the user.
     */
    List<Reservation> getReservationsByUserId (Integer userId);

    /**
     * Retrieve a specific reservation associated with a user by userId and reservationId.
     *
     * @param userId         The unique identifier of the user.
     * @param reservationId The unique identifier of the reservation.
     * @return The Reservation object associated with the user and reservationId.
     */
    Reservation getReservationByUserId(Integer userId, Integer reservationId);

    /**
     * Retrieve a list of reservations associated with a place by placeId.
     *
     * @param placeId The unique identifier of the place.
     * @return A list of Reservation objects associated with the place.
     */
    List<Reservation> getReservationsByPlaceId (Integer placeId);

    /**
     * Retrieve a specific reservation associated with a place by placeId and reservationId.
     *
     * @param placeId         The unique identifier of the place.
     * @param reservationId The unique identifier of the reservation.
     * @return The Reservation object associated with the place and reservationId.
     */
    Reservation getReservationByPlaceId(Integer placeId, Integer reservationId);

    /**
     * Retrieve the place associated with a specific reservation by reservation ID.
     *
     * @param reservationId The ID of the reservation for which to retrieve the place.
     * @return The Place associated with the reservation.
     * @throws ResourceNotFoundException if the reservation or place is not found.
     */
    Place getPlaceByReservationId(Integer reservationId);
}
