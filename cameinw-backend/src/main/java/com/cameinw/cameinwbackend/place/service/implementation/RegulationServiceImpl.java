package com.cameinw.cameinwbackend.place.service.implementation;

import com.cameinw.cameinwbackend.exception.CustomUserFriendlyException;
import com.cameinw.cameinwbackend.exception.ResourceAlreadyExistException;
import com.cameinw.cameinwbackend.exception.ResourceNotFoundException;
import com.cameinw.cameinwbackend.place.model.Facility;
import com.cameinw.cameinwbackend.place.model.Place;
import com.cameinw.cameinwbackend.place.request.RegulationRequest;
import com.cameinw.cameinwbackend.user.model.User;
import com.cameinw.cameinwbackend.place.model.Regulation;
import com.cameinw.cameinwbackend.place.repository.PlaceRepository;
import com.cameinw.cameinwbackend.place.repository.RegulationRepository;
import com.cameinw.cameinwbackend.place.service.RegulationService;
import com.cameinw.cameinwbackend.user.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * The RegulationServiceImpl class implements the RegulationService interface and provides methods for managing regulations
 * and handling regulation-related operations.
 *
 * This service class is annotated with @Service to indicate that it is a Spring service component,
 * allowing it to be automatically discovered by Spring's component scanning and dependency injection mechanism.
 *
 * The @RequiredArgsConstructor annotation is used here to automatically generate a constructor for this class
 * that initializes the final fields marked with the 'final' keyword. This constructor simplifies the injection
 * of dependencies required by this service, such as RegulationRepository, PlaceRepository, and UserRepository.
 */
@RequiredArgsConstructor
@Service
public class RegulationServiceImpl implements RegulationService {
    /**
     * The RegulationRepository responsible for database operations related to regulations.
     */
    private final RegulationRepository regulationRepository;
    /**
     * The PlaceRepository responsible for database operations related to places.
     */
    private final PlaceRepository placeRepository;
    /**
     * The UserRepository responsible for database operations related to users.
     */
    private final UserRepository userRepository;

    /**
     * Retrieves a list of all regulations.
     *
     * @return A list of Regulation objects.
     * @throws ResourceNotFoundException if no regulations were found.
     */
    @Override
    public List<Regulation> getAllRegulations() {
        List<Regulation> regulations = regulationRepository.findAll();
        checkIfRegulationsWereFound(regulations);
        return regulations;
    }

    /**
     * Retrieves a regulation associated with a specific place.
     *
     * @param placeId The ID of the place for which to retrieve the regulation.
     * @return The Regulation object associated with the specified place.
     * @throws ResourceNotFoundException if the regulation is not found for the specified place.
     */
    @Override
    public Regulation getRegulationByPlaceId(Integer placeId) {
        Regulation regulations = regulationRepository.findByPlaceId(placeId);
        checkIfRegulationsWereFoundForThePlace(regulations);
        return regulations;
    }

    /**
     * Creates a new regulation for a place.
     *
     * @param placeId The ID of the place for which to create the regulation.
     * @param regulationRequest The RegulationRequest containing regulation information.
     * @return The newly created Regulation object.
     * @throws ResourceAlreadyExistException if a regulation already exists for the specified place.
     * @throws CustomUserFriendlyException if the user is not the owner of the place.
     */
    @Override
    @Transactional
    public Regulation createRegulation(Integer placeId, RegulationRequest regulationRequest) {
        Place place = getPlaceById(placeId);
        User user = getUserById(regulationRequest.getUser().getId());
        checkUserOwnership(place, user.getId());

        checkIfRegulationsExist(placeId);
        Regulation regulation = createNewRegulation(regulationRequest, place);
        return saveRegulation(regulation);
    }

    /**
     * Updates an existing regulation for a place.
     *
     * @param placeId The ID of the place for which to update the regulation.
     * @param regulationId The ID of the regulation to update.
     * @param regulationRequest The RegulationRequest containing updated regulation information.
     * @return The updated Regulation object.
     * @throws ResourceNotFoundException if the regulation or place is not found.
     * @throws CustomUserFriendlyException if the user is not the owner of the place.
     */
    @Override
    @Transactional
    public Regulation updateRegulation(Integer placeId, Integer regulationId, RegulationRequest regulationRequest) {
        User user = getUserById(regulationRequest.getUser().getId());
        Place place = getPlaceById(placeId);
        checkUserOwnership(place, user.getId());

        Regulation regulations = getRegulationById(regulationId);
        copyOnlyNonNullProperties(regulationRequest, regulations);
        return saveRegulation(regulations);
    }

    /**
     * Deletes a regulation associated with a place.
     *
     * @param placeId The ID of the place for which to delete the regulation.
     * @param regulationId The ID of the regulation to delete.
     * @throws ResourceNotFoundException if the regulation or place is not found.
     */
    @Override
    @Transactional
    public void deleteRegulation(Integer placeId, Integer regulationId) {
        Place place = getPlaceById(placeId);
        Regulation regulation = getRegulationById(regulationId);
        regulationRepository.delete(regulation);
    }

    /**
     * Checks if any regulations were found in the specified list.
     *
     * @param regulations The list of regulations to check.
     * @throws ResourceNotFoundException if no regulations were found.
     */
    private void checkIfRegulationsWereFound(List<Regulation> regulations) {
        if (regulations.isEmpty()) {
            throw new ResourceNotFoundException("No regulations were found.");
        }
    }

    /**
     * Checks if a regulation was found for the specified place.
     *
     * @param regulation The Regulation object to check.
     * @throws ResourceNotFoundException if the regulation is not found for the specified place.
     */
    private void checkIfRegulationsWereFoundForThePlace(Regulation regulation) {
        if (regulation == null) {
            throw new ResourceNotFoundException("Regulation not found for the specified place.");
        }
    }

    /**
     * Retrieves a Regulation entity by its unique ID.
     *
     * @param regulationId The ID of the Regulation to retrieve.
     * @return The Regulation entity if found.
     * @throws ResourceNotFoundException if the regulation with the given ID is not found.
     */
    private Regulation getRegulationById(Integer regulationId) {
        return regulationRepository.findById(regulationId)
                .orElseThrow(() -> new ResourceNotFoundException("Regulation not found."));
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
     * Checks if a user is the owner of a place.
     *
     * @param place The Place object to check ownership for.
     * @param userId The ID of the user to check ownership against.
     * @throws CustomUserFriendlyException if the user is not the owner of the place.
     */
    private void checkUserOwnership(Place place, Integer userId) {
        if (!isUserOwnerOfPlace(place, userId)) {
            throw new CustomUserFriendlyException("User is not the owner of the place.");
        }
    }

    /**
     * Checks if a given user is the owner of a specified place.
     *
     * @param place The Place object to check ownership for.
     * @param userId The ID of the user to check ownership against.
     * @return {@code true} if the user is the owner of the place, {@code false} otherwise.
     */
    private boolean isUserOwnerOfPlace(Place place, Integer userId) {
        User placeOwner = place.getUser();
        return placeOwner != null && placeOwner.getId().equals(userId);
    }

    /**
     * Checks if a regulation already exists for a place.
     *
     * @param placeId The ID of the place to check for existing regulations.
     * @throws ResourceAlreadyExistException if a regulation already exists for this place.
     */
    @Override
    public boolean hasExistingRegulation(Integer placeId) {
        return regulationRepository.existsByPlaceId(placeId);
    }

    /**
     * Checks if regulations already exist for a place and throws an exception if they do.
     *
     * @param placeId The ID of the place to check for existing regulations.
     * @throws ResourceAlreadyExistException if regulations already exist for this place.
     */
    private void checkIfRegulationsExist(Integer placeId) {
        if (hasExistingRegulation(placeId)) {
            throw new ResourceAlreadyExistException("A regulation already exists for this place. You can only update it.");
        }
    }

    /**
     * Creates a new Regulation object based on the provided RegulationRequest and Place.
     *
     * @param regulationRequest The RegulationRequest containing the regulation details.
     * @param place The Place to associate with the new regulation.
     * @return A newly created Regulation object with the specified details.
     */
    private Regulation createNewRegulation(RegulationRequest regulationRequest, Place place) {
        regulationRequest.validate();
        return Regulation.builder()
                .arrivalTime(regulationRequest.getArrivalTime())
                .departureTime(regulationRequest.getDepartureTime())
                .cancellationPolity(regulationRequest.getCancellationPolicy())
                .paymentMethod(regulationRequest.getPaymentMethod())
                .ageRestriction(regulationRequest.isAgeRestriction())
                .arePetsAllowed(regulationRequest.isArePetsAllowed())
                .areEventsAllowed(regulationRequest.isAreEventsAllowed())
                .smokingAllowed(regulationRequest.isSmokingAllowed())
                .quietHours(regulationRequest.getQuietHours())
                .place(place)
                .build();
    }

    /**
     * Copies non-null properties from the RegulationRequest to an existing Regulation object.
     *
     * @param regulationRequest The RegulationRequest containing updated properties.
     * @param regulation The Regulation object to update.
     */
    private void copyOnlyNonNullProperties(RegulationRequest regulationRequest, Regulation regulation) {
        if (regulationRequest.getArrivalTime() != null) {
            regulation.setArrivalTime(regulationRequest.getArrivalTime());
        }
        if (regulationRequest.getDepartureTime() != null) {
            regulation.setDepartureTime(regulationRequest.getDepartureTime());
        }
        if (regulationRequest.getCancellationPolicy() != null) {
            regulation.setCancellationPolity(regulationRequest.getCancellationPolicy());
        }
        if (regulationRequest.getPaymentMethod() != null) {
            regulation.setPaymentMethod(regulationRequest.getPaymentMethod());
        }
        if (regulationRequest.isAgeRestriction() != false) {
            regulation.setAgeRestriction(regulationRequest.isAgeRestriction());
        }
        if (regulationRequest.isArePetsAllowed() != false) {
            regulation.setArePetsAllowed(regulationRequest.isArePetsAllowed());
        }
        if (regulationRequest.isAreEventsAllowed() != false) {
            regulation.setAreEventsAllowed(regulationRequest.isAreEventsAllowed());
        }
        if (regulationRequest.isSmokingAllowed() != false) {
            regulation.setSmokingAllowed(regulationRequest.isSmokingAllowed());
        }
        if (regulationRequest.getQuietHours() != null) {
            regulation.setQuietHours(regulationRequest.getQuietHours());
        }

    }

    /**
     * Saves a Regulation object to the repository and returns the saved instance.
     *
     * @param regulation The Regulation object to save.
     * @return The saved Regulation object.
     */
    private Regulation saveRegulation(Regulation regulation) {
        return regulationRepository.save(regulation);
    }
}
