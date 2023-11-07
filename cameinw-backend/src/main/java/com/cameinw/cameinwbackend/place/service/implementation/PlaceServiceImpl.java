package com.cameinw.cameinwbackend.place.service.implementation;

import com.cameinw.cameinwbackend.exception.CustomUserFriendlyException;
import com.cameinw.cameinwbackend.exception.ResourceNotFoundException;
import com.cameinw.cameinwbackend.place.model.Place;
import com.cameinw.cameinwbackend.place.repository.PlaceRepository;
import com.cameinw.cameinwbackend.place.request.PlaceRequest;
import com.cameinw.cameinwbackend.place.service.PlaceService;
import com.cameinw.cameinwbackend.user.enums.Role;
import com.cameinw.cameinwbackend.user.model.Reservation;
import com.cameinw.cameinwbackend.user.model.User;
import com.cameinw.cameinwbackend.user.repository.ReservationRepository;
import com.cameinw.cameinwbackend.user.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * The PlaceServiceImpl class implements the PlaceService interface and provides methods for managing places
 * and handling place-related operations.
 *
 * This service class is annotated with @Service to indicate that it is a Spring service component,
 * allowing it to be automatically discovered by Spring's component scanning and dependency injection mechanism.
 *
 * The @RequiredArgsConstructor annotation is used here to automatically generate a constructor for this class
 * that initializes the final fields marked with the 'final' keyword. This constructor simplifies the injection
 * of dependencies required by this service, such as UserRepository, PlaceRepository, and ReservationRepository.
 */
@RequiredArgsConstructor
@Service
public class PlaceServiceImpl implements PlaceService {
    /**
     * The UserRepository responsible for database operations related to users.
     */
    private final UserRepository userRepository;

    /**
     * The PlaceRepository responsible for database operations related to places.
     */
    private final PlaceRepository placeRepository;

    /**
     * The ReservationRepository responsible for database operations related to reservations.
     */
    private final ReservationRepository reservationRepository;


    /**
     * Retrieves a list of all places.
     *
     * @return A list of Place objects.
     * @throws ResourceNotFoundException if no places were found.
     */
    @Override
    public List<Place> getAllPlaces() {
        List<Place> places = placeRepository.findAll();
        checkIfPlacesWereFound(places);
        return places;
    }

    /**
     * Retrieves a place by its ID.
     *
     * @param placeId The ID of the place to retrieve.
     * @return The Place object if found.
     * @throws ResourceNotFoundException if the place with the given ID is not found.
     */
    @Override
    public Place getPlaceByPlaceId(Integer placeId) {
        return getPlaceById(placeId);
    }

    /**
     * Creates a new place.
     *
     * @param placeRequest The PlaceRequest object containing the place information.
     * @return The newly created Place object.
     */
    @Override
    @Transactional
    public Place createPlace(PlaceRequest placeRequest) {
        User user = getUserById(placeRequest.getUser().getId());
        checkRole(user);

        Place newPlace = createNewPlace(placeRequest, user);
        return savePlace(newPlace);
    }

    /**
     * Updates an existing place.
     *
     * @param placeId The ID of the place to update.
     * @param placeRequest The PlaceRequest object containing the updated place information.
     * @return The updated Place object.
     * @throws ResourceNotFoundException if the place with the given ID is not found.
     * @throws CustomUserFriendlyException if the user is not the owner of the place.
     */
    @Override
    @Transactional
    public Place updatePlace(Integer placeId, PlaceRequest placeRequest) {
        User user = getUserById(placeRequest.getUser().getId());
        Place place = getPlaceById(placeId);
        checkUserOwnership(place, user.getId());

        copyOnlyNonNullProperties(place, placeRequest);
        return savePlace(place);
    }

    /**
     * Deletes a place.
     *
     * @param placeId The ID of the place to delete.
     * @throws ResourceNotFoundException if the place with the given ID is not found.
     */
    @Override
    @Transactional
    public void deletePlace(Integer placeId) {
        Place place = getPlaceById(placeId);
        placeRepository.delete(place);
    }

    /**
     * Retrieves the owner of a place by place ID.
     *
     * @param placeId The ID of the place.
     * @return The User object representing the owner of the place.
     * @throws ResourceNotFoundException if the place with the given ID is not found.
     */
    @Override
    public User getOwner(Integer placeId) {
        Place place = getPlaceById(placeId);
        return place.getUser();
    }

    /**
     * Checks if a list of places is empty and throws a ResourceNotFoundException if no places were found.
     *
     * @param places The list of Place objects to check.
     * @throws ResourceNotFoundException if the list of places is empty.
     */
    private void checkIfPlacesWereFound(List<Place> places) {
        if (places.isEmpty()) {
            throw new ResourceNotFoundException("No places were found.");
        }
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
        System.out.println("get place by placeId: " + placeId);
        return placeRepository.findById(placeId)
                .orElseThrow(() -> new ResourceNotFoundException("Place not found."));
    }

    /**
     * Checks if a user has the role of OWNER.
     *
     * @param user The User object to check.
     * @throws CustomUserFriendlyException if the user is not an owner.
     */
    private void checkRole(User user) {
        if (user.getRole() != Role.OWNER) {
            throw new CustomUserFriendlyException("User is not an owner.");
        }
    }

    /**
     * Checks if a user is the owner of a given place.
     *
     * @param place The Place object to check ownership for.
     * @param userId The ID of the user to compare with the place owner.
     * @throws CustomUserFriendlyException if the user is not the owner of the place.
     */
    private void checkUserOwnership(Place place, Integer userId) {
        if (!isUserOwnerOfPlace(place, userId)) {
            throw new CustomUserFriendlyException("User is not the owner of the place.");
        }
    }

    /**
     * Checks if a user is the owner of a given place.
     *
     * @param place The Place object to check ownership for.
     * @param userId The ID of the user to compare with the place owner.
     * @return true if the user is the owner of the place, false otherwise.
     */
    private boolean isUserOwnerOfPlace(Place place, Integer userId) {
        User placeOwner = place.getUser();
        return placeOwner != null && placeOwner.getId().equals(userId);
    }

    /**
     * Creates a new Place object based on the provided PlaceRequest and User, while validating the PlaceRequest.
     *
     * @param placeRequest The PlaceRequest object containing the place information.
     * @param user The User who is the owner of the place.
     * @return The newly created Place object.
     */
    private Place createNewPlace(PlaceRequest placeRequest, User user) {
        placeRequest.validate();
        return Place.builder()
                .name(placeRequest.getName())
                .propertyType(placeRequest.getPropertyType())
                .description(placeRequest.getDescription())
                .cost(placeRequest.getCost())
                .country(placeRequest.getCountry())
                .city(placeRequest.getCity())
                .address(placeRequest.getAddress())
                .latitude(placeRequest.getLatitude())
                .longitude(placeRequest.getLongitude())
                .area(placeRequest.getArea())
                .guests(placeRequest.getGuests())
                .bedrooms(placeRequest.getBedrooms())
                .beds(placeRequest.getBeds())
                .bathrooms(placeRequest.getBathrooms())
                .user(user)
                .build();
    }

    /**
     * Copies non-null properties from a PlaceRequest to a Place object.
     *
     * @param place The Place object to copy properties to.
     * @param placeRequest The PlaceRequest object to copy properties from.
     */
    private void copyOnlyNonNullProperties(Place place, PlaceRequest placeRequest) {
        if (placeRequest.getName() != null) {
            place.setName(placeRequest.getName());
        }
        if (placeRequest.getPropertyType() != null) {
            place.setPropertyType(placeRequest.getPropertyType());
        }
        if (placeRequest.getDescription() != null) {
            place.setDescription(placeRequest.getDescription());
        }
        if (placeRequest.getCost() != null) {
            place.setCost(placeRequest.getCost());
        }
        if (placeRequest.getCountry() != null) {
            place.setCountry(placeRequest.getCountry());
        }
        if (placeRequest.getCity() != null) {
            place.setCity(placeRequest.getCity());
        }
        if (placeRequest.getAddress() != null) {
            place.setAddress(placeRequest.getAddress());
        }
        if (placeRequest.getLatitude() != null) {
            place.setLatitude(placeRequest.getLatitude());
        }
        if (placeRequest.getLongitude() != null) {
            place.setLongitude(placeRequest.getLongitude());
        }
        if (placeRequest.getArea() != null) {
            place.setArea(placeRequest.getArea());
        }
        if (placeRequest.getGuests() != null) {
            place.setGuests(placeRequest.getGuests());
        }
        if (placeRequest.getBedrooms() != null) {
            place.setBedrooms(placeRequest.getBedrooms());
        }
        if (placeRequest.getBeds() != null) {
            place.setBeds(placeRequest.getBeds());
        }
        if (placeRequest.getBathrooms() != null) {
            place.setBathrooms(placeRequest.getBathrooms());
        }
    }

    /**
     * Saves a Place object in the repository.
     *
     * @param place The Place object to be saved.
     * @return The saved Place object.
     */
    private Place savePlace(Place place) {return  placeRepository.save(place);}

    /**
     * Retrieves a list of available places in a specific city and country for a given number of guests and date range.
     *
     * @param city The city where the places are located.
     * @param country The country where the places are located.
     * @param guests The number of guests the places should accommodate.
     * @param checkIn The check-in date for the reservation.
     * @param checkOut The check-out date for the reservation.
     * @return A list of available Place objects that meet the criteria.
     */
    @Override
    public List<Place> getAvailablePlaces(String city, String country, Integer guests, Date checkIn, Date checkOut) {
        List<Place> allReservedPlaces = new ArrayList<>();
        List<Place> places = placeRepository.findPlacesByCountryAndCity(country, city, guests);

        for (Place place : places) {
            List<Reservation> allReservations =
                    reservationRepository.findBetweenDates(
                            checkIn,
                            checkOut,
                            place
                    );
            allReservedPlaces.addAll(allReservations.stream()
                    .map(Reservation::getPlace)
                    .collect(Collectors.toList())
            );
        }
        places.removeAll(allReservedPlaces);

        if (!places.isEmpty()) {
            return places;
        } else {
            return places;
        }
    }
}
