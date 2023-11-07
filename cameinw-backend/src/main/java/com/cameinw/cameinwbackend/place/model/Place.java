package com.cameinw.cameinwbackend.place.model;

import com.cameinw.cameinwbackend.image.model.Image;
import com.cameinw.cameinwbackend.user.model.Reservation;
import com.cameinw.cameinwbackend.user.model.Review;
import com.fasterxml.jackson.annotation.JsonIgnore;

import com.cameinw.cameinwbackend.user.model.User;
import com.cameinw.cameinwbackend.place.enums.PropertyType;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;


/**
 * The Place class represents a location or property available for reservations.
 * Each place has a unique id, name, property type, description, main image, cost, country, city, address, latitude and longitude.
 * It also has information about the number of area, guests, bedrooms, bedrooms, beds and bathrooms it can accommodate.
 * Additionally, it includes references to the enums who owns the place, and collections of images, ratings,
 * facilities, regulations, and reservations associated with the place.
 *
 * The class uses Lombok library annotations for convenient boilerplate code reduction and Hibernate annotations for ORM mapping.
 *
 * - @Data - Generates getters, setters, equals, hashcode, and a toString methods.
 * - @Builder - Provides a build pattern for constructing a Place object.
 * - @NoArgsConstructor - Generates a no-arg constructor, needed for Hibernate to instantiate the object.
 * - @AllArgsConstructor - Generates a constructor with all properties as arguments.
 * - @Entity - Marks this class as an entity bean, so it can be persisted in the database.
 * - @Table - Specifies the "places" table in the database where records of this class will be stored.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name="places")
public class Place {
    /**
     * The unique ID of the place.
     * Generated automatically by the persistence provider using an identity column in the database.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    /**
     * The name of the place.
     * This field is mapped to the "name" column in the "places" table.
     */
    @Column(name = "name")
    private String name;

    /**
     * The type of the property.
     * This field is mapped to the "type" column in the "places" table and is stored as a string representation of the PropertyType enum.
     */
    @Column(name = "type")
    @Enumerated(EnumType.STRING)
    private PropertyType propertyType;

    /**
     * A description of the place.
     * This field is mapped to the "description" column in the "places" table.
     */
    @Column(name = "description")
    private String description;

    /**
     * The filename of the main image associated with the place.
     * This field is mapped to the "main_image" column in the "places" table.
     */
    @Column(name = "main_image")
    private String mainImage;

    /**
     * The cost of staying at the place.
     * This field is mapped to the "cost" column in the "places" table.
     */
    @Column(name = "cost")
    private Integer cost;

    /**
     * The country where the place is located.
     * This field is mapped to the "country" column in the "places" table.
     */
    @Column(name = "country")
    private String country;

    /**
     * The city where the place is located.
     * This field is mapped to the "city" column in the "places" table.
     */
    @Column(name = "city")
    private String city;

    /**
     * The address of the place.
     * This field is mapped to the "address" column in the "places" table.
     */
    @Column(name = "address")
    private String address;

    /**
     * The latitude coordinate of the place's location.
     * This field is mapped to the "latitude" column in the "places" table.
     */
    @Column(name = "latitude")
    private Double latitude;

    /**
     * The longitude coordinate of the place's location.
     * This field is mapped to the "longitude" column in the "places" table.
     */
    @Column(name = "longitude")
    private Double longitude;

    /**
     * The total area in square meters of the place.
     * This field is mapped to the "area" column in the "places" table.
     */
    @Column(name = "area")
    private Integer area;

    /**
     * The number of guests that the place can accommodate.
     * This field is mapped to the "guests" column in the "places" table.
     */
    @Column(name = "guests")
    private Integer guests;

    /**
     * The number of bedrooms available in the place.
     * This field is mapped to the "bedrooms" column in the "places" table.
     */
    @Column(name = "bedrooms")
    private Integer bedrooms;

    /**
     * The number of beds available in the place.
     * This field is mapped to the "beds" column in the "places" table.
     */
    @Column(name = "beds")
    private Integer beds;

    /**
     * The number of bathrooms available in the place.
     * This field is mapped to the "bathrooms" column in the "places" table.
     */
    @Column(name = "bathrooms")
    private Integer bathrooms;


    /**
     * The user who owns the place.
     * Mapped as a many-to-one relationship to the User entity.
     * This field represents the owner of the place and is not nullable.
     */
    @ManyToOne
    @JsonIgnore
    private User user;

    /**
     * A collection of images associated with the place.
     * Mapped as a one-to-many relationship to the Image entity.
     * The "mappedBy" attribute refers to the "place" field in the Image entity,
     * indicating that the Image entity is the owner of the relationship.
     */
    @OneToMany(mappedBy = "place")
    private List<Image> images;

    /**
     * A collection of ratings associated with the place.
     * Mapped as a one-to-many relationship to the Review entity.
     * The "mappedBy" attribute refers to the "place" field in the Review entity,
     * indicating that the Review entity is the owner of the relationship.
     */
    @OneToMany(mappedBy = "place")
    private List<Review> reviews;

    /**
     * A collection of reservations associated with the place.
     * Mapped as a one-to-many relationship to the Reservation entity.
     * The "mappedBy" attribute refers to the "place" field in the Reservation entity,
     * indicating that the Reservation entity is the owner of the relationship.
     */
    @OneToMany(mappedBy = "place")
    //@JsonIgnore
    private List<Reservation> reservations;

    /**
     * A reference to regulations associated with the place.
     * Mapped as a one-to-one relationship to the Regulation entity.
     * The "mappedBy" attribute refers to the "place" field in the Regulation entity,
     * indicating that the Regulation entity is the owner of the relationship.
     */
    @OneToOne(mappedBy = "place")
    private Regulation regulations;

    /**
     * A reference to facilities associated with the place.
     * Mapped as a one-to-one relationship to the Facility entity.
     * The "mappedBy" attribute refers to the "place" field in the Facility entity,
     * indicating that the Facility entity is the owner of the relationship.
     */
    @OneToOne(mappedBy = "place")
    private Facility facilities;
}
