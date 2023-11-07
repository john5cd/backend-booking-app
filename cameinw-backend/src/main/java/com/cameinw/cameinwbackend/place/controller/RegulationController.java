package com.cameinw.cameinwbackend.place.controller;

import com.cameinw.cameinwbackend.exception.CustomUserFriendlyException;
import com.cameinw.cameinwbackend.exception.ResourceAlreadyExistException;
import com.cameinw.cameinwbackend.exception.ResourceNotFoundException;
import com.cameinw.cameinwbackend.place.model.Regulation;
import com.cameinw.cameinwbackend.place.request.RegulationRequest;
import com.cameinw.cameinwbackend.place.service.RegulationService;
import com.cameinw.cameinwbackend.response.GenericResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * The RegulationController class defines RESTful endpoints for managing and retrieving regulations for places.
 *
 * This class is annotated with:
 * - @RestController: Marks this class as a REST controller, allowing it to handle HTTP requests.
 * - @RequestMapping("/api"): Specifies the base URL path for all endpoints in this controller.
 * - @RequiredArgsConstructor: automatically generates a constructor for this class that injects the final fields declared in the class.
 */
@RequiredArgsConstructor
@RestController
@RequestMapping("/api")
public class RegulationController {
    /**
     * The RegulationService that provides methods for managing and retrieving regulations for places.
     */
    private final RegulationService regulationService;

    /**
     * Retrieves a list of all regulations.
     *
     * @return A ResponseEntity containing the list of regulations if found, or an error response if not found.
     * @throws ResourceNotFoundException if no regulations are found.
     */
    @GetMapping("/regulations") // !!!CHECK OK!!!
    public ResponseEntity<Object> getAllRegulations() {
        GenericResponse genericResponse = new GenericResponse();
        try {
            List<Regulation> regulations = regulationService.getAllRegulations();
            return new ResponseEntity<>(regulations, HttpStatus.OK);
        } catch (ResourceNotFoundException ex) {
            genericResponse.setMessage(ex.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(genericResponse);
        }
    }

    /**
     * Retrieves regulations for a specific place by its ID.
     *
     * @param placeId The ID of the place for which regulations are to be retrieved.
     * @return A ResponseEntity containing the regulations for the specified place if found, or an error response if not found.
     * @throws ResourceNotFoundException if the place or regulations are not found.
     * @throws CustomUserFriendlyException if there is a user-friendly exception.
     */
    @GetMapping("/places/{place_id}/regulations") // !!!CHECK OK!!!
    public ResponseEntity<Object> getRegulationByPlaceId(@PathVariable("place_id") Integer placeId) {
        GenericResponse genericResponse = new GenericResponse();
        try {
            Regulation regulation = regulationService.getRegulationByPlaceId(placeId);
            return new ResponseEntity<>(regulation, HttpStatus.OK);
        } catch (ResourceNotFoundException ex) {
            genericResponse.setMessage(ex.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(genericResponse);
        } catch (CustomUserFriendlyException ex) {
            genericResponse.setMessage(ex.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(genericResponse);
        }
    }

    /**
     * Creates a new regulation for a specific place.
     *
     * @param placeId The ID of the place for which the regulation is to be created.
     * @param regulationRequest The RegulationRequest object containing the regulation data to be created.
     * @return A ResponseEntity with a success message or an error response if validation fails or an error occurs.
     * @throws ResourceNotFoundException if the place is not found.
     * @throws ResourceAlreadyExistException if the regulation already exists for the place.
     * @throws CustomUserFriendlyException if there is a validation error or user-friendly exception.
     */
    @PostMapping("/places/{place_id}/regulations") // !!!CHECK OK!!!
    public ResponseEntity<Object> createRegulation(
            @PathVariable("place_id") Integer placeId,
            @Valid @RequestBody RegulationRequest regulationRequest) {
        GenericResponse genericResponse = new GenericResponse();
        try {
            Regulation createdRegulation = regulationService.createRegulation(placeId, regulationRequest);
            genericResponse.setMessage("Regulation successfully created.");
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
     * Updates an existing regulation for a specific place.
     *
     * @param placeId The ID of the place to which the regulation belongs.
     * @param regulationId The ID of the regulation to update.
     * @param regulationRequest The RegulationRequest object containing the updated regulation data.
     * @return A ResponseEntity with a success message or an error response if not found or an error occurs.
     * @throws ResourceNotFoundException if the place or regulation is not found.
     * @throws CustomUserFriendlyException if there is a validation error or user-friendly exception.
     */
    @PutMapping("/places/{place_id}/regulations/{regulation_id}") // !!!CHECK OK!!!
    public ResponseEntity<Object> updateRegulation(
            @PathVariable("place_id") Integer placeId,
            @PathVariable("regulation_id") Integer regulationId,
            @Valid  @RequestBody RegulationRequest regulationRequest
    ) {
        GenericResponse genericResponse = new GenericResponse();
        try {
            Regulation updatedRegulation = regulationService.updateRegulation(placeId, regulationId, regulationRequest);
            genericResponse.setMessage("Regulation successfully updated.");
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
     * Deletes a regulation for a specific place by its ID.
     *
     * @param placeId The ID of the place to which the regulation belongs.
     * @param regulationId The ID of the regulation to delete.
     * @return A ResponseEntity with a success message or an error response if not found.
     * @throws ResourceNotFoundException if the place or regulation is not found.
     * @throws CustomUserFriendlyException if there is a user-friendly exception.
     */
    @DeleteMapping("/places/{place_id}/regulations/{regulation_id}") // !!!CHECK OK!!!
    public ResponseEntity<Object> deleteRegulation(
            @PathVariable("place_id") Integer placeId,
            @PathVariable("regulation_id") Integer regulationId) {
        GenericResponse genericResponse = new GenericResponse();
        try {
            regulationService.deleteRegulation(placeId, regulationId);
            genericResponse.setMessage("Regulation successfully deleted.");
            return ResponseEntity.ok(genericResponse);
        } catch (ResourceNotFoundException ex) {
            genericResponse.setMessage(ex.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(genericResponse);
        } catch (CustomUserFriendlyException ex) {
            genericResponse.setMessage(ex.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(genericResponse);
        }
    }

}
