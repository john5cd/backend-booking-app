package com.cameinw.cameinwbackend.place.service;

import com.cameinw.cameinwbackend.place.model.Regulation;
import com.cameinw.cameinwbackend.place.request.RegulationRequest;

import java.util.List;

/**
 * The RegulationService interface defines methods for managing regulations related to places.
 * It is used for operations such as retrieving, creating, updating, and deleting regulations.
 */
public interface RegulationService {
    /**
     * Retrieve a list of all regulations.
     *
     * @return A list of Regulation objects.
     */
    List<Regulation> getAllRegulations();

    /**
     * Retrieve the regulation associated with a specific place by placeId.
     *
     * @param placeId The unique identifier of the place.
     * @return The Regulation object associated with the provided placeId.
     */
    Regulation getRegulationByPlaceId(Integer placeId);

    /**
     * Create a new regulation for a place.
     *
     * @param placeId           The unique identifier of the place.
     * @param regulationRequest The request object containing regulation information.
     * @return The newly created Regulation object.
     */
    Regulation createRegulation(Integer placeId, RegulationRequest regulationRequest);

    /**
     * Update an existing regulation for a place.
     *
     * @param placeId           The unique identifier of the place.
     * @param regulationId       The unique identifier of the regulation to update.
     * @param regulationRequest The request object containing updated regulation information.
     * @return The updated Regulation object.
     */
    Regulation updateRegulation(Integer placeId, Integer regulationId, RegulationRequest regulationRequest);


    /**
     * Delete a regulation associated with a specific place by placeId and regulationId.
     *
     * @param placeId     The unique identifier of the place.
     * @param regulationId The unique identifier of the regulation to delete.
     */
    void deleteRegulation(Integer placeId, Integer regulationId);

    /**
     * Check if a place has an existing regulation.
     *
     * @param placeId The unique identifier of the place.
     * @return true if a regulation exists for the place, false otherwise.
     */
    boolean hasExistingRegulation(Integer placeId);
}
