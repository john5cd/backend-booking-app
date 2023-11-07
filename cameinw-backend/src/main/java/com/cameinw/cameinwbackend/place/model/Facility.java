package com.cameinw.cameinwbackend.place.model;

import com.fasterxml.jackson.annotation.JsonIgnore;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

/**
 * The Facility class represents the amenities available at a place.
 * Each facility has a unique ID and boolean fields indicating the presence of various entities.
 *
 * The class uses Lombok library annotations for convenient boilerplate code reduction and Hibernate annotations for ORM mapping.
 *
 * - @Data - Generates getters, setters, equals, hashcode, and a toString methods.
 * - @Builder - Provides a build pattern for constructing a Facility object.
 * - @NoArgsConstructor - Generates a no-arg constructor, needed for Hibernate to instantiate the object.
 * - @AllArgsConstructor - Generates a constructor with all properties as arguments.
 * - @Entity - Marks this class as an entity bean, so it can be persisted in the database.
 * -@Table - Specifies the "facilities" table in the database where records of this class will be stored.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name="facilities")

public class Facility {
    /**
     * The unique ID of the facility.
     * Generated automatically by the persistence provider using an identity column in the database.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    /**
     * Indicates whether the place has free parking.
     * This field is mapped to the "free_parking" column in the "facilities" table.
     */
    @Column(name = "free_parking")
    private boolean hasFreeParking;

    /**
     * Indicates whether the place is non-smoking.
     * This field is mapped to the "non_smoking" column in the "facilities" table.
     */
    @Column(name = "non_smoking")
    private boolean isNonSmoking;

    /**
     * Indicates whether the place has free Wi-Fi.
     * This field is mapped to the "free_WiFi" column in the "facilities" table.
     */
    @Column(name = "free_WiFi")
    private boolean hasFreeWiFi;

    /**
     * Indicates whether the place has breakfast.
     * This field is mapped to the "breakfast" column in the "facilities" table.
     */
    @Column(name = "breakfast")
    private boolean hasbreakfast;

    /**
     * Indicates whether the place has balcony.
     * This field is mapped to the "balcony" column in the "facilities" table.
     */
    @Column(name = "balcony")
    private boolean hasbalcony;

    /**
     * Indicates whether the place has swimming pool.
     * This field is mapped to the "swimming_pool" column in the "facilities" table.
     */
    @Column(name = "swimming_pool")
    private boolean hasSwimmingPool;

    /**
     * The place to which these facilities are associated.
     * Mapped as a one-to-one relationship to the Place entity, with the foreign key being the place_id column.
     * If the referenced Place entity is deleted, this Facility entity will also be deleted due to the cascade delete setting.
     */
    @OneToOne
    @JoinColumn(name = "place_id", referencedColumnName = "id")
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JsonIgnore
    private Place place;
}
