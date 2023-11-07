package com.cameinw.cameinwbackend.place.projection;

import com.cameinw.cameinwbackend.place.enums.PropertyType;

/**
 * The PlaceProjection interface defines a projection for retrieving specific properties of a Place entity.
 * It provides methods for obtaining the place's ID, name, country, city, address, number of guests, cost, bathrooms,
 * bedrooms, description, and property type.
 */
public interface PlaceProjection {
    /**
     * Gets the ID of the place.
     *
     * @return The ID of the place.
     */
    Integer getId();

    /**
     * Gets the name of the place.
     *
     * @return The name of the place.
     */
    String getName();

    /**
     * Gets the country where the place is located.
     *
     * @return The country of the place.
     */
    String getCountry();

    /**
     * Gets the city where the place is located.
     *
     * @return The city of the place.
     */
    String getCity();

    /**
     * Gets the address of the place.
     *
     * @return The address of the place.
     */
    String getAddress();

    /**
     * Gets the number of guests the place can accommodate.
     *
     * @return The number of guests the place can accommodate.
     */
    Integer getGuests();

    /**
     * Gets the cost of the place per night.
     *
     * @return The cost of the place per night.
     */
    Integer getCost();

    /**
     * Gets the number of bathrooms in the place.
     *
     * @return The number of bathrooms in the place.
     */
    Integer getBathrooms();

    /**
     * Gets the number of bedrooms in the place.
     *
     * @return The number of bedrooms in the place.
     */
    Integer getBedrooms();

    /**
     * Gets the description of the place.
     *
     * @return The description of the place.
     */
    String getDescription();

    /**
     * Gets the property type of the place.
     *
     * @return The property type of the place.
     */
    Enum<PropertyType> getPropertyType();
}
