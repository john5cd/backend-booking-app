package com.cameinw.cameinwbackend.place.service;

import com.cameinw.cameinwbackend.place.model.Facility;
import com.cameinw.cameinwbackend.place.request.FacilityRequest;

import java.util.List;

/**
 * The FacilityService interface defines methods for managing facilities related to places.
 * It is used for operations such as retrieving, creating, updating, and deleting facilities.
 */
public interface FacilityService {
    /**
     * Retrieve a list of all facilities.
     *
     * @return A list of Facility objects.
     */
    List<Facility> getAllFacilities();

    /**
     * Retrieve the facility associated with a specific place by placeId.
     *
     * @param placeId The unique identifier of the place.
     * @return The Facility object associated with the provided placeId.
     */
    Facility getFacilityByPlaceId(Integer placeId);

    /**
     * Create a new facility for a place.
     *
     * @param placeId           The unique identifier of the place.
     * @param facilityRequest   The request object containing facility information.
     * @return The newly created Facility object.
     */
    Facility createFacility(Integer placeId, FacilityRequest facilityRequest);

    /**
     * Update an existing facility for a place.
     *
     * @param placeId           The unique identifier of the place.
     * @param facilityId        The unique identifier of the facility to update.
     * @param facilityRequest   The request object containing updated facility information.
     * @return The updated Facility object.
     */
    Facility updateFacility(Integer placeId, Integer facilityId, FacilityRequest facilityRequest);

    /**
     * Delete a facility associated with a specific place by placeId and facilityId.
     *
     * @param placeId     The unique identifier of the place.
     * @param facilityId  The unique identifier of the facility to delete.
     */
    void deleteFacility(Integer placeId, Integer facilityId);

    /**
     * Check if a place has an existing facility.
     *
     * @param placeId The unique identifier of the place.
     * @return true if a facility exists for the place, false otherwise.
     */
    boolean hasExistingFacility(Integer placeId);
}
