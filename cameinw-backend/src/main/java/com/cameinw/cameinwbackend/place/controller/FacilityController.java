package com.cameinw.cameinwbackend.place.controller;

import com.cameinw.cameinwbackend.exception.CustomUserFriendlyException;
import com.cameinw.cameinwbackend.exception.ResourceAlreadyExistException;
import com.cameinw.cameinwbackend.exception.ResourceNotFoundException;
import com.cameinw.cameinwbackend.place.model.Facility;
import com.cameinw.cameinwbackend.place.request.FacilityRequest;
import com.cameinw.cameinwbackend.place.service.FacilityService;
import com.cameinw.cameinwbackend.response.GenericResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * The FacilityController class defines RESTful endpoints for managing and retrieving facilities for places.
 *
 * This class is annotated with:
 * - @RestController: Marks this class as a REST controller, allowing it to handle HTTP requests.
 * - @RequestMapping("/api"): Specifies the base URL path for all endpoints in this controller.
 * - @RequiredArgsConstructor: automatically generates a constructor for this class that injects the final fields declared in the class.
 */
@RequiredArgsConstructor
@RestController
@RequestMapping("/api")
public class FacilityController {
    /**
     * The FacilityService that provides methods for managing and retrieving facilities for places.
     */
    private final FacilityService facilityService;

    /**
     * Retrieves a list of all facilities.
     *
     * @return A ResponseEntity containing the list of facilities if found, or an error response if not found.
     * @throws ResourceNotFoundException if no facilities are found.
     */
    @GetMapping("/facilities") // ----- check ok ------
    public ResponseEntity<Object> getAllFacilities() {
        GenericResponse genericResponse = new GenericResponse();
        try {
            List<Facility> facilities = facilityService.getAllFacilities();
            return new ResponseEntity<>(facilities, HttpStatus.OK);
        } catch (ResourceNotFoundException ex) {
            genericResponse.setMessage(ex.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(genericResponse);
        }
    }

    /**
     * Retrieves facilities for a specific place by its ID.
     *
     * @param placeId The ID of the place for which facilities are to be retrieved.
     * @return A ResponseEntity containing the facilities for the specified place if found, or an error response if not found.
     * @throws ResourceNotFoundException if the place or facilities are not found.
     * @throws CustomUserFriendlyException if there is a user-friendly exception.
     */
    @GetMapping("/places/{place_id}/facilities") // !!! CHECK OK !!!
    public ResponseEntity<Object> getFacilitiesByPlaceId(@PathVariable("place_id") Integer placeId) {
        GenericResponse genericResponse = new GenericResponse();
        try {
            Facility facility = facilityService.getFacilityByPlaceId(placeId);
            return new ResponseEntity<>(facility, HttpStatus.OK);
        } catch (ResourceNotFoundException ex) {
            genericResponse.setMessage(ex.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(genericResponse);
        } catch (CustomUserFriendlyException ex) {
            genericResponse.setMessage(ex.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(genericResponse);
        }
    }

    /**
     * Creates a new facility for a specific place.
     *
     * @param placeId The ID of the place for which the facility is to be created.
     * @param facilityRequest The FacilityRequest object containing the facility data to be created.
     * @return A ResponseEntity with a success message or an error response if validation fails or an error occurs.
     * @throws ResourceNotFoundException if the place is not found.
     * @throws ResourceAlreadyExistException if the facility already exists for the place.
     * @throws CustomUserFriendlyException if there is a validation error or user-friendly exception.
     */
    @PostMapping("/places/{place_id}/facilities") // !!! CHECK OK !!!
    public ResponseEntity<Object> createFacility(@PathVariable("place_id") Integer placeId, @Valid @RequestBody FacilityRequest facilityRequest) {
        GenericResponse genericResponse = new GenericResponse();
        try {
            Facility createdFacility = facilityService.createFacility(placeId, facilityRequest);
            genericResponse.setMessage("Facility successfully created.");
            return ResponseEntity.status(HttpStatus.CREATED).body(genericResponse);
        } catch (ResourceNotFoundException ex) {
            genericResponse.setMessage(ex.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(genericResponse);
        }catch (ResourceAlreadyExistException ex) {
            genericResponse.setMessage(ex.getMessage());
            return ResponseEntity.status(HttpStatus.CONFLICT).body(genericResponse);
        } catch (CustomUserFriendlyException ex) {
            genericResponse.setMessage(ex.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(genericResponse);
        }
    }

    /**
     * Updates an existing facility for a specific place.
     *
     * @param placeId          The ID of the place to which the facility belongs.
     * @param facilityId       The ID of the facility to update.
     * @param facilityRequest  The FacilityRequest object containing the updated facility data.
     * @return A ResponseEntity with a success message or an error response if not found or an error occurs.
     * @throws ResourceNotFoundException      if the place or facility is not found.
     * @throws CustomUserFriendlyException     if there is a validation error or user-friendly exception.
     */
    @PutMapping("/places/{place_id}/facilities/{facility_id}") // !!! CHECK OK !!!
    public ResponseEntity<Object> updateFacility(
            @PathVariable("place_id") Integer placeId,
            @PathVariable("facility_id") Integer facilityId,
            @Valid  @RequestBody FacilityRequest facilityRequest
    ) {
        GenericResponse genericResponse = new GenericResponse();
        try {
            Facility updatedFacility = facilityService.updateFacility(placeId, facilityId, facilityRequest);
            genericResponse.setMessage("Facility successfully updated.");
            return ResponseEntity.status(HttpStatus.OK).body(genericResponse);
        } catch (ResourceNotFoundException ex) {
            genericResponse.setMessage(ex.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(genericResponse);
        } catch (CustomUserFriendlyException ex) {
            genericResponse.setMessage(ex.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(genericResponse);
        }
    }

    /**
     * Deletes a facility for a specific place by its ID.
     *
     * @param placeId    The ID of the place to which the facility belongs.
     * @param facilityId The ID of the facility to delete.
     * @return A ResponseEntity with a success message or an error response if not found.
     * @throws ResourceNotFoundException  if the place or facility is not found.
     * @throws CustomUserFriendlyException if there is a user-friendly exception.
     */
    @DeleteMapping("/places/{place_id}/facilities/{facility_id}") // !!! CHECK OK !!!
    public ResponseEntity<Object> deleteFacility(@PathVariable("place_id") Integer placeId, @PathVariable("facility_id") Integer facilityId) {
        GenericResponse genericResponse = new GenericResponse();
        try {
            facilityService.deleteFacility(placeId, facilityId);
            genericResponse.setMessage("Facility successfully deleted.");
            return ResponseEntity.status(HttpStatus.NO_CONTENT).body(genericResponse);
        } catch (ResourceNotFoundException ex) {
            genericResponse.setMessage(ex.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(genericResponse);
        } catch (CustomUserFriendlyException ex) {
            genericResponse.setMessage(ex.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(genericResponse);
        }
    }

}
