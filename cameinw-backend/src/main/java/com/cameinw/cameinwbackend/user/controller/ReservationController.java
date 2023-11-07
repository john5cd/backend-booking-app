package com.cameinw.cameinwbackend.user.controller;
import com.cameinw.cameinwbackend.exception.CustomUserFriendlyException;
import com.cameinw.cameinwbackend.exception.ResourceNotFoundException;
import com.cameinw.cameinwbackend.place.model.Facility;
import com.cameinw.cameinwbackend.place.model.Place;
import com.cameinw.cameinwbackend.place.request.FacilityRequest;
import com.cameinw.cameinwbackend.place.request.RegulationRequest;
import com.cameinw.cameinwbackend.response.GenericResponse;
import com.cameinw.cameinwbackend.user.model.Reservation;
import com.cameinw.cameinwbackend.user.model.User;
import com.cameinw.cameinwbackend.user.request.ReservationRequest;
import com.cameinw.cameinwbackend.user.service.ReservationService;
import com.cameinw.cameinwbackend.user.service.ReviewService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.parameters.P;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * The ReservationController class defines RESTful endpoints for managing and retrieving user reservations.
 *
 * This class is annotated with:
 * - @RestController: Marks this class as a REST controller, allowing it to handle HTTP requests.
 * - @RequestMapping("/api"): Specifies the base URL path for all endpoints in this controller.
 * - @RequiredArgsConstructor: automatically generates a constructor for this class that injects the final fields declared in the class.
 */
@RequiredArgsConstructor
@RestController
@RequestMapping("/api")
public class ReservationController {
    /**
     * The ReservationService that provides methods for managing and retrieving user reservations.
     */
    private final ReservationService reservationService;

    /**
     * Creates a new reservation for a place.
     *
     * @param placeId The ID of the place for which the reservation is being created.
     * @param reservationRequest The ReservationRequest object containing the reservation data.
     * @return A ResponseEntity with a success message or an error response if not found or an error occurs.
     * @throws ResourceNotFoundException if the place is not found.
     * @throws CustomUserFriendlyException if there is a validation error or user-friendly exception.
     */
    @PostMapping("/places/{place_id}/reservations") // !!! CHECK OK!!
    public ResponseEntity<Object> makeReservation(
            @PathVariable("place_id") Integer placeId,
            @Valid @RequestBody ReservationRequest reservationRequest) {
        GenericResponse genericResponse = new GenericResponse();
        try {
            Reservation createReservation = reservationService.makeReservation(placeId, reservationRequest);
            genericResponse.setMessage("Reservation successfully created.");
            return ResponseEntity.status(HttpStatus.CREATED).body(genericResponse);
        } catch (ResourceNotFoundException ex) {
            genericResponse.setMessage(ex.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(genericResponse);
        } catch (CustomUserFriendlyException ex) {
            genericResponse.setMessage(ex.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(genericResponse);
        }
    }

    /**
     * Retrieves reservations made by a specific user.
     *
     * @param userId The ID of the user.
     * @return A ResponseEntity containing the list of reservations made by the user if found, or an error response if not found.
     * @throws ResourceNotFoundException if the user or reservations are not found.
     */
    @GetMapping("/users/{user_id}/reservations") // !!! CHECK OK!!
    public ResponseEntity<Object> getReservationsByUserId(@PathVariable("user_id") Integer userId) {
        GenericResponse genericResponse = new GenericResponse();
        try {
            List<Reservation> reservations = reservationService.getReservationsByUserId(userId);
            return ResponseEntity.status(HttpStatus.OK).body(reservations);
        } catch (ResourceNotFoundException ex) {
            genericResponse.setMessage(ex.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(genericResponse);
        }
    }

    /**
     * Retrieves a specific reservation made by a user.
     *
     * @param userId The ID of the user who made the reservation.
     * @param reservationId The ID of the reservation to retrieve.
     * @return A ResponseEntity containing the reservation data if found, or an error response if not found.
     * @throws ResourceNotFoundException if the reservation or user is not found.
     */
    @GetMapping("/users/{user_id}/reservations/{reservation_id}") // !!! CHECK OK!!
    public ResponseEntity<Object> getReservationByUserId(
            @PathVariable("user_id") Integer userId,
            @PathVariable("reservation_id") Integer reservationId) {
        GenericResponse genericResponse = new GenericResponse();
        try {
            Reservation reservation = reservationService.getReservationByUserId(userId, reservationId);
            return ResponseEntity.status(HttpStatus.OK).body(reservation);
        } catch (ResourceNotFoundException ex) {
            genericResponse.setMessage(ex.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(genericResponse);
        }
    }

    /**
     * Retrieves reservations for a specific place.
     *
     * @param placeId The ID of the place.
     * @return A ResponseEntity containing the list of reservations for the place if found, or an error response if not found.
     * @throws ResourceNotFoundException if the place or reservations are not found.
     */
    @GetMapping("/places/{place_id}/reservations") // !!! CHECK OK!!
    public ResponseEntity<Object> getReservationsByPlaceId(@PathVariable("place_id") Integer placeId) {
        GenericResponse genericResponse = new GenericResponse();
        try {
            List<Reservation> reservations = reservationService.getReservationsByPlaceId(placeId);
            return ResponseEntity.status(HttpStatus.OK).body(reservations);
        } catch (ResourceNotFoundException ex) {
            genericResponse.setMessage(ex.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(genericResponse);
        }
    }

    /**
     * Retrieves a specific reservation for a place.
     *
     * @param placeId The ID of the place.
     * @param reservationId The ID of the reservation to retrieve.
     * @return A ResponseEntity containing the reservation data if found, or an error response if not found.
     * @throws ResourceNotFoundException if the reservation or place is not found.
     */
    @GetMapping("/places/{place_id}/reservations/{reservation_id}") // !!! CHECK OK!!
    public ResponseEntity<Object> getReservationByPlaceId(
            @PathVariable("place_id") Integer placeId,
            @PathVariable("reservation_id") Integer reservationId) {
        GenericResponse genericResponse = new GenericResponse();
        try {
            Reservation reservation = reservationService.getReservationByPlaceId(placeId, reservationId);
            return ResponseEntity.status(HttpStatus.OK).body(reservation);
        } catch (ResourceNotFoundException ex) {
            genericResponse.setMessage(ex.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(genericResponse);
        }
    }

    /**
     * Retrieves the place associated with a specific reservation by reservation ID.
     *
     * @param reservationId The ID of the reservation for which to retrieve the place.
     * @return A ResponseEntity containing the Place data if found, or an error response if not found.
     * @throws ResourceNotFoundException if the reservation or place is not found.
     */
    @GetMapping("/reservations/{reservation_id}/place")
    public ResponseEntity<Object> getPlaceByReservationId(
            @PathVariable("reservation_id") Integer reservationId) {
        GenericResponse genericResponse = new GenericResponse();
        try {
            Place place = reservationService.getPlaceByReservationId(reservationId);
            return ResponseEntity.status(HttpStatus.OK).body(place);
        } catch (ResourceNotFoundException ex) {
            genericResponse.setMessage(ex.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(genericResponse);
        }
    }
}
