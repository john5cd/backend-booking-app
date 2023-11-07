package com.cameinw.cameinwbackend.place.service;

import com.cameinw.cameinwbackend.place.model.Place;
import com.cameinw.cameinwbackend.place.request.PlaceRequest;
import com.cameinw.cameinwbackend.user.model.User;

import java.util.Date;
import java.util.List;

/**
 * The PlaceService interface defines methods for managing places and handling place-related operations.
 * It is used for operations such as retrieving, creating, updating, and deleting places.
 */
public interface PlaceService {
    /**
     * Retrieve a list of all places.
     *
     * @return A list of Place objects.
     */
    List<Place> getAllPlaces();

    /**
     * Retrieve a place by its unique identifier.
     *
     * @param placeId The unique identifier of the place.
     * @return The Place object associated with the provided placeId.
     */
    Place getPlaceByPlaceId(Integer placeId);

    /**
     * Create a new place.
     *
     * @param placeRequest The request object containing place information.
     * @return The newly created Place object.
     */
    Place createPlace(PlaceRequest placeRequest);

    /**
     * Update an existing place.
     *
     * @param placeId      The unique identifier of the place to update.
     * @param placeRequest The request object containing updated place information.
     * @return The updated Place object.
     */
    Place updatePlace(Integer placeId, PlaceRequest placeRequest);

    /**
     * Delete a place by its unique identifier.
     *
     * @param placeId The unique identifier of the place to delete.
     */
    void deletePlace(Integer placeId);

    /**
     * Get the owner of a place by its unique identifier.
     *
     * @param placeId The unique identifier of the place.
     * @return The User object representing the owner of the place.
     */
    User getOwner(Integer placeId);

    /**
     * Get a list of available places based on search criteria.
     *
     * @param city     The city where the places are located.
     * @param country  The country where the places are located.
     * @param guests   The number of guests the places can accommodate.
     * @param checkIn  The check-in date for availability.
     * @param checkOut The check-out date for availability.
     * @return A list of available Place objects.
     */
    List<Place> getAvailablePlaces(String city, String country, Integer guests, Date checkIn, Date checkOut);

}
