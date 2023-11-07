package com.cameinw.cameinwbackend.place.service.implementation;

import com.cameinw.cameinwbackend.exception.CustomUserFriendlyException;
import com.cameinw.cameinwbackend.exception.ResourceAlreadyExistException;
import com.cameinw.cameinwbackend.exception.ResourceNotFoundException;
import com.cameinw.cameinwbackend.place.model.Facility;
import com.cameinw.cameinwbackend.place.model.Place;
import com.cameinw.cameinwbackend.place.repository.FacilityRepository;
import com.cameinw.cameinwbackend.place.repository.PlaceRepository;
import com.cameinw.cameinwbackend.place.request.FacilityRequest;
import com.cameinw.cameinwbackend.place.service.FacilityService;
import com.cameinw.cameinwbackend.user.model.User;
import com.cameinw.cameinwbackend.user.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * The FacilityServiceImpl class implements the FacilityService interface and provides methods for managing facilities
 * and handling facility-related operations.
 *
 * This service class is annotated with @Service to indicate that it is a Spring service component,
 * allowing it to be automatically discovered by Spring's component scanning and dependency injection mechanism.
 *
 * The @RequiredArgsConstructor annotation is used here to automatically generate a constructor for this class
 * that initializes the final fields marked with the 'final' keyword. This constructor simplifies the injection
 * of dependencies required by this service, such as FacilityRepository, PlaceRepository, and UserRepository.
 */
@RequiredArgsConstructor
@Service
public class FacilityServiceImpl implements FacilityService {
    /**
     * The FacilityRepository responsible for database operations related to facilities.
     */
    private final FacilityRepository facilityRepository;
    /**
     * The PlaceRepository responsible for database operations related to places.
     */
    private final PlaceRepository placeRepository;
    /**
     * The UserRepository responsible for database operations related to users.
     */
    private final UserRepository userRepository;

    /**
     * Retrieves a list of all facilities.
     *
     * @return A list of Facility objects.
     * @throws ResourceNotFoundException if no facilities were found.
     */
    @Override
    public List<Facility> getAllFacilities() {
        List<Facility> facilities = facilityRepository.findAll();
        checkIfFacilitiesWereFound(facilities);
        return facilities;
    }

    /**
     * Retrieves a facility by its associated place ID.
     *
     * @param placeId The ID of the associated place.
     * @return The Facility object associated with the given place ID.
     * @throws ResourceNotFoundException if the facility was not found for the specified place.
     */
    @Override
    public Facility getFacilityByPlaceId(Integer placeId) {
        Facility facility = facilityRepository.findByPlaceId(placeId);
        checkIfFacilitiesWereFoundForThePlace(facility);
        return facility;
    }

    /**
     * Creates a new facility for the specified place.
     *
     * @param placeId The ID of the associated place.
     * @param facilityRequest  The FacilityRequest object containing facility details.
     * @return The created Facility object.
     * @throws ResourceNotFoundException if the place or user was not found.
     * @throws CustomUserFriendlyException if the user is not the owner of the place.
     * @throws ResourceAlreadyExistException if a facility already exists for this place.
     */
    @Override
    @Transactional
    public Facility createFacility(Integer placeId, FacilityRequest facilityRequest) {
        Place place = getPlaceById(placeId);
        User user = getUserById(facilityRequest.getUser().getId());
        checkUserOwnership(place, user.getId());

        checkIfFacilitiesExist(placeId);
        Facility facility = createNewFacility(facilityRequest, place);
        return saveFacility(facility);
    }

    /**
     * Updates an existing facility for the specified place.
     *
     * @param placeId The ID of the associated place.
     * @param facilityId The ID of the facility to be updated.
     * @param facilityRequest The FacilityRequest object containing updated facility details.
     * @return The updated Facility object.
     * @throws ResourceNotFoundException  if the place, user, or facility was not found.
     * @throws CustomUserFriendlyException if the user is not the owner of the place.
     */
    @Override
    @Transactional
    public Facility updateFacility(Integer placeId, Integer facilityId, FacilityRequest facilityRequest) {
        User user = getUserById(facilityRequest.getUser().getId());
        Place place = getPlaceById(placeId);
        checkUserOwnership(place, user.getId());

        Facility facility = getFacilityById(facilityId);
        copyOnlyNonNullProperties(facilityRequest, facility);
        return saveFacility(facility);
    }

    /**
     * Deletes a facility for the specified place.
     *
     * @param placeId The ID of the associated place.
     * @param facilityId The ID of the facility to be deleted.
     * @throws ResourceNotFoundException if the place or facility was not found.
     */
    @Override
    @Transactional
    public void deleteFacility(Integer placeId, Integer facilityId) {
        Place place = getPlaceById(placeId);
        Facility facility = getFacilityById(facilityId);
        facilityRepository.delete(facility);
    }

    /**
     * Checks if facilities were found in the provided list.
     *
     * @param facilities The list of Facility objects to check.
     * @throws ResourceNotFoundException if no facilities were found.
     */
    private void checkIfFacilitiesWereFound(List<Facility> facilities) {
        if (facilities.isEmpty()) {
            throw new ResourceNotFoundException("No facilities were found.");
        }
    }

    /**
     * Checks if a facility was found for the specified place.
     *
     * @param facility The Facility object to check.
     * @throws ResourceNotFoundException if the facility was not found for the specified place.
     */
    private void checkIfFacilitiesWereFoundForThePlace(Facility facility) {
        if (facility == null) {
            throw new ResourceNotFoundException("Facility not found for the specified place.");
        }
    }

    /**
     * Retrieves a facility by its ID.
     *
     * @param facilityId The ID of the facility to retrieve.
     * @return The Facility object with the specified ID.
     * @throws ResourceNotFoundException if the facility was not found.
     */
    private Facility getFacilityById(Integer facilityId) {
        return facilityRepository.findById(facilityId)
                .orElseThrow(() -> new ResourceNotFoundException("Facility not found."));
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
     * Checks if the user is the owner of the specified place.
     *
     * @param place  The Place object to check ownership against.
     * @param userId The ID of the user to check ownership for.
     * @throws CustomUserFriendlyException if the user is not the owner of the place.
     */
    private void checkUserOwnership(Place place, Integer userId) {
        if (!isUserOwnerOfPlace(place, userId)) {
            throw new CustomUserFriendlyException("User is not the owner of the place.");
        }
    }

    /**
     * Checks if the specified user is the owner of the given place.
     *
     * @param place  The Place object to check ownership against.
     * @param userId The ID of the user to check ownership for.
     * @return {@code true} if the user is the owner of the place, {@code false} otherwise.
     */
    private boolean isUserOwnerOfPlace(Place place, Integer userId) {
        User placeOwner = place.getUser();
        return placeOwner != null && placeOwner.getId().equals(userId);
    }

    /**
     * Checks if there is an existing facility for the specified place.
     *
     * @param placeId The ID of the place to check for an existing facility.
     * @return {@code true} if a facility exists for the place, {@code false} otherwise.
     */
    @Override
    public boolean hasExistingFacility(Integer placeId) {
        return facilityRepository.existsByPlaceId(placeId);
    }

    /**
     * Checks if a facility already exists for the specified place.
     *
     * @param placeId The ID of the place to check for an existing facility.
     * @throws ResourceAlreadyExistException if a facility already exists for this place.
     */
    private void checkIfFacilitiesExist(Integer placeId) {
        if (hasExistingFacility(placeId)) {
            throw new ResourceAlreadyExistException("A facility already exists for this place. You can only update it.");
        }
    }

    /**
     * Creates a new Facility instance based on the provided FacilityRequest and Place.
     *
     * @param facilityRequest The FacilityRequest containing facility information.
     * @param place The Place to associate with the new Facility.
     * @return A new Facility instance.
     */
    private Facility createNewFacility(FacilityRequest facilityRequest, Place place) {
        return Facility.builder()
                .hasFreeParking(facilityRequest.isHasFreeParking())
                .isNonSmoking(facilityRequest.isNonSmoking())
                .hasFreeWiFi(facilityRequest.isHasFreeWiFi())
                .hasbreakfast(facilityRequest.getHasbreakfast())
                .hasbalcony(facilityRequest.isHasbalcony())
                .hasSwimmingPool(facilityRequest.isHasSwimmingPool())
                .place(place)
                .build();
    }

    /**
     * Copies non-null properties from a FacilityRequest to an existing Facility instance.
     *
     * @param facilityRequest The FacilityRequest containing updated facility properties.
     * @param facility The existing Facility instance to update.
     */
    private void copyOnlyNonNullProperties(FacilityRequest facilityRequest, Facility facility) {
        if (facilityRequest.isHasFreeParking() != false) {
            facility.setHasFreeParking(facilityRequest.isHasFreeParking());
        }
        if (facilityRequest.isNonSmoking() != false) {
            facility.setNonSmoking(facilityRequest.isNonSmoking());
        }
        if (facilityRequest.isHasFreeWiFi() != false) {
            facility.setHasFreeWiFi(facilityRequest.isHasFreeWiFi());
        }
        if (facilityRequest.getHasbreakfast() != null) {
            facility.setHasbreakfast(facilityRequest.getHasbreakfast());
        }
        if (facilityRequest.isHasbalcony() != false) {
            facility.setHasbalcony(facilityRequest.isHasbalcony());
        }
        if (facilityRequest.isHasSwimmingPool() != false) {
            facility.setHasSwimmingPool(facilityRequest.isHasSwimmingPool());
        }
    }

    /**
     * Saves a Facility instance to the database.
     *
     * @param facility The Facility instance to be saved.
     * @return The saved Facility instance.
     */
    private Facility saveFacility(Facility facility) {
        return facilityRepository.save(facility);
    }


}
