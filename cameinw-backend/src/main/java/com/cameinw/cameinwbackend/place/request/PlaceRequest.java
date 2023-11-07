package com.cameinw.cameinwbackend.place.request;

import com.cameinw.cameinwbackend.place.enums.PropertyType;
import com.cameinw.cameinwbackend.user.model.User;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Arrays;
import java.util.stream.Collectors;

/**
 * The PlaceRequest class represents a request object for creating or updating a place listing.
 * It includes properties such as name, property type, description, cost, location details, and more.
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PlaceRequest {
    /**
     * The name of the place.
     */
    @NotBlank(message = "Name cannot be null or empty.")
    private String name;

    /**
     * The type of the property, e.g., APARTMENT, HOUSE, etc.
     */
    @NotNull(message = "Property type cannot be null.")
    private PropertyType propertyType;

    /**
     * A description of the place.
     */
    @NotBlank(message = "Description cannot be null or empty.")
    private String description;

    /**
     * The cost of renting the place.
     */
    @NotNull(message = "Cost cannot be null.")
    @PositiveOrZero(message = "Cost cannot be negative.")
    private Integer cost;

    /**
     * The country where the place is located.
     */
    @NotBlank(message = "Country cannot be null or empty.")
    private String country;

    /**
     * The city where the place is located.
     */
    @NotBlank(message = "City cannot be null or empty.")
    private String city;

    /**
     * The address of the place.
     */
    @NotBlank(message = "Address cannot be null or empty.")
    private String address;

    /**
     * The latitude coordinate of the place's location.
     */
    @NotNull(message = "Latitude cannot be null.")
    private Double latitude;

    /**
     * The longitude coordinate of the place's location.
     */
    @NotNull(message = "Longitude cannot be null.")
    private Double longitude;

    /**
     * The area of the place in square meters.
     */
    @NotNull(message = "Area cannot be null.")
    @PositiveOrZero(message = "Area cannot be negative.")
    private Integer area;

    /**
     * The maximum number of guests the place can accommodate.
     */
    @NotNull(message = "Guests cannot be null.")
    @PositiveOrZero(message = "Guests number cannot be negative.")
    private Integer guests;

    /**
     * The number of bedrooms in the place.
     */
    @NotNull(message = "Bedrooms cannot be null.")
    @PositiveOrZero(message = "Bedrooms number cannot be negative.")
    private Integer bedrooms;

    /**
     * The number of beds in the place.
     */
    @NotNull(message = "Beds cannot be null.")
    @PositiveOrZero(message = "Beds number cannot be negative.")
    private Integer beds;

    /**
     * The number of bathrooms in the place.
     */
    @NotNull(message = "Bathrooms cannot be null.")
    @PositiveOrZero(message = "Bathrooms number cannot be negative.")
    private Integer bathrooms;

    /**
     * The user who creates the place listing.
     */
    @NotNull(message = "User cannot be null.")
    private User user;

    /**
     * Validates the request object, ensuring that the property type is valid.
     *
     * @throws IllegalArgumentException if the property type is not valid.
     */
    public void validate() {
        if (!isValidPropertyType(propertyType)) {
            throw new IllegalArgumentException("Invalid property type. Available property types are: " + getPropertyTypesList());        }
    }

    /**
     * Checks if the provided property type is valid.
     *
     * @param propertyType The property type to check.
     * @return true if the property type is valid, false otherwise.
     */
    private boolean isValidPropertyType(PropertyType propertyType) {
        return Arrays.asList(PropertyType.values()).contains(propertyType);
    }

    /**
     * Generates a comma-separated list of available property types.
     *
     * @return A string containing available property types.
     */
    private String getPropertyTypesList() {
        return Arrays.stream(PropertyType.values())
                .map(Enum::name)
                .collect(Collectors.joining(", "));
    }
}


// ------------------------ EXAMPLES -----------------------

//           --Create Place--
//
//{
//        "name": "Example Place",
//        "propertyType": "APARTMENT",
//        "description": "A cozy apartment in the city center.",
//        "cost": 100,
//        "country": "Greece",
//        "city": "Athens",
//        "address": "123 Main Street",
//        "latitude": 40.7128,
//        "longitude": -74.0060,
//        "area": 80,
//        "guests": 2,
//        "bedrooms": 1,
//        "beds": 1,
//        "bathrooms": 1,
//        "user": {
//            "id": 2
//        }
//}

//           --Update Place--
//
//{
//        "cost": 200,
//        "user": {
//            "id": 2
//        }
//}


