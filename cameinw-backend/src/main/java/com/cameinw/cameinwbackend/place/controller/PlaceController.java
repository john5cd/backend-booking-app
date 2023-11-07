package com.cameinw.cameinwbackend.place.controller;

import com.cameinw.cameinwbackend.exception.CustomUserFriendlyException;
import com.cameinw.cameinwbackend.exception.ResourceNotFoundException;
import com.cameinw.cameinwbackend.place.model.Place;
import com.cameinw.cameinwbackend.place.request.PlaceRequest;
import com.cameinw.cameinwbackend.place.service.PlaceService;
import com.cameinw.cameinwbackend.response.GenericResponse;
import com.cameinw.cameinwbackend.user.dto.UserDTO;
import com.cameinw.cameinwbackend.user.model.User;
import com.cameinw.cameinwbackend.utilities.MapToDTO;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

/**
 * The PlaceController class defines RESTful endpoints for managing and retrieving places.
 *
 * This class is annotated with:
 * - @RestController: Marks this class as a REST controller, allowing it to handle HTTP requests.
 * - @RequestMapping("/api/places"): Specifies the base URL path for all endpoints in this controller.
 * - @RequiredArgsConstructor: automatically generates a constructor for this class that injects the final fields declared in the class.
 */
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/places")
public class PlaceController {

    /**
     * The PlaceService that provides methods for managing and retrieving places.
     */
    private final PlaceService placeService;

    /**
     * Retrieves a list of all places.
     *
     * @return A ResponseEntity containing the list of places if found, or an error response if not found.
     * @throws ResourceNotFoundException if no places are found.
     */
    @GetMapping()  // !!!CHECK OK!!!
    public ResponseEntity<Object> getAllPlaces() {
        GenericResponse genericResponse = new GenericResponse();
        try {
            List<Place> places = placeService.getAllPlaces();
            return new ResponseEntity<>(places, HttpStatus.OK);
        } catch (ResourceNotFoundException ex) {
            genericResponse.setMessage(ex.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(genericResponse);
        }
    }

    /**
     * Creates a new place.
     *
     * @param placeRequest The PlaceRequest object containing the place data to be created.
     * @return A ResponseEntity with a success message or an error response if validation fails or an error occurs.
     * @throws CustomUserFriendlyException if there is a validation error or user-friendly exception.
     * @throws ResourceNotFoundException if a related resource is not found.
     * @throws IllegalArgumentException if an invalid argument is provided.
     */
    @PostMapping() // !!!CHECK OK!!!
    public ResponseEntity<Object> createPlace(@Valid @RequestBody PlaceRequest placeRequest) {

        GenericResponse genericResponse = new GenericResponse();
        try {
            Place createdPlace = placeService.createPlace(placeRequest);
            genericResponse.setMessage("Place successfully created.");
            return ResponseEntity.status(HttpStatus.CREATED).body(genericResponse);
        } catch (CustomUserFriendlyException ex) {
            genericResponse.setMessage(ex.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(genericResponse);
        } catch (ResourceNotFoundException ex) {
            genericResponse.setMessage(ex.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(genericResponse);
        } catch (IllegalArgumentException ex) {
            genericResponse.setMessage(ex.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(genericResponse);
        }
    }

    /**
     * Retrieves a place by its ID.
     *
     * @param placeId The ID of the place to retrieve.
     * @return A ResponseEntity containing the place data if found, or an error response if not found.
     * @throws ResourceNotFoundException if the place is not found.
     */
    @GetMapping("/{place_id}") // !!!CHECK OK!!!
    public ResponseEntity<Object> getPlaceById (@PathVariable("place_id") Integer placeId) {
        GenericResponse genericResponse = new GenericResponse();
        try {
            return new ResponseEntity<>(placeService.getPlaceByPlaceId(placeId), HttpStatus.OK);
        } catch (ResourceNotFoundException ex) {
            genericResponse.setMessage(ex.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(genericResponse);
        }

    }

    /**
     * Updates an existing place.
     *
     * @param placeId      The ID of the place to update.
     * @param placeRequest The PlaceRequest object containing the updated place data.
     * @return A ResponseEntity with a success message or an error response if not found or an error occurs.
     * @throws ResourceNotFoundException if the place is not found.
     * @throws CustomUserFriendlyException if there is a validation error or user-friendly exception.
     */
    @PutMapping("/{place_id}") // !!!CHECK OK!!!
    public ResponseEntity<Object> updatePlace(@PathVariable("place_id") Integer placeId, @RequestBody PlaceRequest placeRequest) {
        GenericResponse genericResponse = new GenericResponse();
        try {
            Place updated = placeService.updatePlace(placeId, placeRequest);
            genericResponse.setMessage("Place successfully updated");
            return ResponseEntity.status(HttpStatus.NO_CONTENT).body(genericResponse);
        } catch (ResourceNotFoundException ex) {
            genericResponse.setMessage(ex.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(genericResponse);
        } catch (CustomUserFriendlyException ex) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(genericResponse);
        }
    }

    /**
     * Deletes a place by its ID.
     *
     * @param placeId The ID of the place to delete.
     * @return A ResponseEntity with a success message or an error response if not found.
     * @throws ResourceNotFoundException if the place is not found.
     */
    @DeleteMapping("/{place_id}") // !!!CHECK OK!!!
    public ResponseEntity<Object> deletePlace(@PathVariable("place_id") Integer placeId) {
        GenericResponse genericResponse = new GenericResponse();
        try {
            placeService.deletePlace(placeId);
            genericResponse.setMessage("Place deleted successfully.");
            return ResponseEntity.status(HttpStatus.NO_CONTENT).body(genericResponse);
        } catch (ResourceNotFoundException ex) {
            genericResponse.setMessage(ex.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(genericResponse);
        }
    }

    /**
     * Retrieves the owner of a place by its ID.
     *
     * @param placeId The ID of the place.
     * @return A ResponseEntity containing the owner data if found, or an error response if not found.
     * @throws ResourceNotFoundException if the owner is not found.
     */
    @GetMapping("/{place_id}/owner") // !!!CHECK OK!!!
    public ResponseEntity<Object> getOwner(@PathVariable("place_id") Integer placeId) {
        GenericResponse genericResponse = new GenericResponse();
        try {
            User user = placeService.getOwner(placeId);
            UserDTO userDTO = MapToDTO.mapUserToDTO(user);
            return new ResponseEntity<>(userDTO, HttpStatus.OK);
        } catch (ResourceNotFoundException ex) {
            genericResponse.setMessage(ex.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(genericResponse);
        }
    }

    /**
     * Retrieves available places based on search criteria.
     *
     * @param city      The city where the place is located.
     * @param country   The country where the place is located.
     * @param guests    The number of guests the place can accommodate.
     * @param checkIn   The check-in date.
     * @param checkOut  The check-out date.
     * @return A ResponseEntity containing the list of available places if found, or an empty response if none are available.
     */
    @GetMapping("/availability/{city}/{country}/{guests}/{checkIn}/{checkOut}")
    public ResponseEntity<List<Place>> getAvailablePlaces(
            @PathVariable("city") String city,
            @PathVariable("country") String country,
            @PathVariable("guests") Integer guests,
            @PathVariable("checkIn") @DateTimeFormat(pattern =
                    "yyyy-MM-dd") Date checkIn,
            @PathVariable("checkOut") @DateTimeFormat(pattern =
                    "yyyy-MM-dd") Date checkOut
    ) {
        List<Place> availablePlaces =
                placeService.getAvailablePlaces(city, country, guests, checkIn,
                        checkOut);
        if (availablePlaces.size()>0) {
            return ResponseEntity.ok(availablePlaces);
        } else {
            return ResponseEntity.noContent().build();
        }
    }

}
