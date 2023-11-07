package com.cameinw.cameinwbackend.user.service.implementation;

import com.cameinw.cameinwbackend.exception.CustomUserFriendlyException;
import com.cameinw.cameinwbackend.exception.ResourceNotFoundException;
import com.cameinw.cameinwbackend.place.model.Place;
import com.cameinw.cameinwbackend.place.repository.PlaceRepository;
import com.cameinw.cameinwbackend.user.model.Reservation;
import com.cameinw.cameinwbackend.user.model.User;
import com.cameinw.cameinwbackend.user.repository.ReservationRepository;
import com.cameinw.cameinwbackend.user.repository.UserRepository;
import com.cameinw.cameinwbackend.user.request.ReservationRequest;
import com.cameinw.cameinwbackend.user.service.ReservationService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * The ReservationServiceImpl class implements the ReservationService interface and provides methods for managing reservations
 * and handling reservation-related operations.
 *
 * This service class is annotated with @Service to indicate that it is a Spring service component,
 * allowing it to be automatically discovered by Spring's component scanning and dependency injection mechanism.
 *
 * The @RequiredArgsConstructor annotation is used here to automatically generate a constructor for this class
 * that initializes the final fields marked with the 'final' keyword. This constructor simplifies the injection
 * of dependencies required by this service, such as ReservationRepository, UserRepository, and PlaceRepository.
 */
@RequiredArgsConstructor
@Service
public class ReservationServiceImpl implements ReservationService {
    /**
     * The ReservationRepository responsible for database operations related to reservations.
     */
    private final ReservationRepository reservationRepository;
    /**
     * The UserRepository responsible for database operations related to users.
     */
    private final UserRepository userRepository;
    /**
     * The PlaceRepository responsible for database operations related to places.
     */
    private final PlaceRepository placeRepository;

    /**
     * Makes a reservation for a user at a specified place based on the provided reservation request.
     *
     * @param placeId The ID of the place for which the reservation is being made.
     * @param reservationRequest The reservation request containing check-in and check-out dates.
     * @return The newly created Reservation entity representing the reservation.
     */
    public Reservation makeReservation(Integer placeId, ReservationRequest reservationRequest) {
        Place place = getPlaceById(placeId);
        User user = getUserById(reservationRequest.getUser().getId());

        checkReservationValidation(reservationRequest, place);
        Reservation reservation = createReservation(reservationRequest, place, user);
        return saveReservation(reservation);
    }

    /**
     * Retrieves a list of reservations made by a user with the specified user ID.
     *
     * @param userId The ID of the user for whom reservations are being retrieved.
     * @return A list of Reservation entities made by the user.
     * @throws ResourceNotFoundException if no reservations are found for the user.
     */
    @Override
    public List<Reservation> getReservationsByUserId (Integer userId){
        User user = getUserById(userId);
        List<Reservation> reservations = reservationRepository.findByUser(user);
        checkIfReservationsExistForResource(reservations);
        return reservations;
    }

    /**
     * Retrieves a specific reservation made by a user with the specified user ID.
     *
     * @param userId The ID of the user who made the reservation.
     * @param reservationId The ID of the reservation to retrieve.
     * @return The Reservation entity representing the requested reservation.
     */
    @Override
    public Reservation getReservationByUserId (Integer userId, Integer reservationId){
        User user = getUserById(userId);
        Reservation reservation = getReservationById(reservationId);
        return reservation;
    }

    /**
     * Retrieves a list of reservations made for a specified place based on the place ID.
     *
     * @param placeId The ID of the place for which reservations are being retrieved.
     * @return A list of Reservation entities made for the place.
     * @throws ResourceNotFoundException if no reservations are found for the place.
     */
    @Override
    public List<Reservation> getReservationsByPlaceId (Integer placeId){
        Place place = getPlaceById(placeId);
        List<Reservation> reservations = reservationRepository.findByPlace(place);
        checkIfReservationsExistForResource(reservations);
        return reservations;
    }

    /**
     * Retrieves a specific reservation made for a specified place based on the place ID and reservation ID.
     *
     * @param placeId The ID of the place for which the reservation was made.
     * @param reservationId The ID of the reservation to retrieve.
     * @return The Reservation entity representing the requested reservation for the specified place.
     */
    @Override
    public Reservation getReservationByPlaceId (Integer placeId, Integer reservationId){
        Place place = getPlaceById(placeId);
        Reservation reservation = getReservationById(reservationId);
        return reservation;
    }

    /**
     * Retrieves the place associated with a specific reservation by reservation ID.
     *
     * @param reservationId The ID of the reservation for which to retrieve the place.
     * @return The Place associated with the reservation.
     * @throws ResourceNotFoundException if the reservation or place is not found.
     */
    @Override
    public Place getPlaceByReservationId(Integer reservationId) {
        Reservation reservation = getReservationById(reservationId);
        return reservation.getPlace();
    }

    /**
     * Retrieves a Place entity by its unique ID.
     *
     * @param placeId The ID of the place to retrieve.
     * @return The Place entity if found.
     * @throws ResourceNotFoundException if the place with the given ID is not found.
     */
    private Place getPlaceById(Integer placeId) {
        return placeRepository.findById(placeId)
                .orElseThrow(() -> new ResourceNotFoundException("Place not found."));
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
     * Retrieves a Reservation entity by its unique ID.
     *
     * @param reservationId The ID of the place to retrieve.
     * @return The Reservation entity if found.
     * @throws ResourceNotFoundException if the reservation with the given ID is not found.
     */
    private Reservation getReservationById(Integer reservationId) {
        return reservationRepository.findById(reservationId)
                .orElseThrow(() -> new ResourceNotFoundException("Reservation not found."));
    }

    /**
     * Checks if there are reservations found for a resource (e.g., user or place).
     *
     * @param reservations The list of reservations to check.
     * @throws ResourceNotFoundException if no reservations are found for the resource.
     */
    public void checkIfReservationsExistForResource(List<Reservation> reservations) {
        if (reservations.isEmpty()) {
            throw new ResourceNotFoundException("No reservations found.");
        }
    }

    /**
     * Checks if a reservation request is valid, i.e., there are no conflicting reservations
     * and the check-in date is before the check-out date.
     *
     * @param reservationRequest The reservation request to validate.
     * @param place The place for which the reservation is being made.
     * @return true if the reservation is valid, false otherwise.
     */
    private boolean isValidReservation(ReservationRequest reservationRequest, Place place) {
        List<Reservation> existingReservations = reservationRepository.findBetweenDates(
                reservationRequest.getCheckIn(),
                reservationRequest.getCheckOut(),
                place);

        return existingReservations.isEmpty()
                && reservationRequest.getCheckIn().compareTo(reservationRequest.getCheckOut()) < 0;
    }

    /**
     * Checks if a reservation request is valid for the specified place.
     *
     * @param reservationRequest The reservation request to validate.
     * @param place The place for which the reservation is being made.
     * @throws CustomUserFriendlyException if the reservation is not valid.
     */
    private void checkReservationValidation(ReservationRequest reservationRequest, Place place) {
        if (!isValidReservation(reservationRequest, place)) {
            throw new CustomUserFriendlyException("Failed to create reservation.");
        }
    }

    /**
     * Creates a new reservation based on the provided ReservationRequest, place, and user.
     *
     * @param reservationRequest The reservation request containing check-in and check-out dates.
     * @param place The place for which the reservation is being made.
     * @param user The user making the reservation.
     * @return The newly created Reservation entity.
     */
    private Reservation createReservation(ReservationRequest reservationRequest, Place place, User user) {
        return Reservation.builder()
                .checkIn(reservationRequest.getCheckIn())
                .checkOut(reservationRequest.getCheckOut())
                .user(user)
                .place(place)
                .build();
    }

    /**
     * Saves a Reservation entity to the database.
     *
     * @param reservation The Reservation entity to be saved.
     * @return The saved Reservation entity.
     */
    private Reservation saveReservation(Reservation reservation) {
        return reservationRepository.save(reservation);
    }
}
