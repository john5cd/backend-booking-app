package com.cameinw.cameinwbackend.place.model;

import com.cameinw.cameinwbackend.place.enums.PaymentMethod;
import com.cameinw.cameinwbackend.user.model.User;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

/**
 * The Regulation class represents regulations and policies associated with a place.
 * Each regulation has a unique ID and various fields specifying policies such as arrival time, payment methods, and more.
 *
 * The class uses Lombok library annotations for convenient boilerplate code reduction and Hibernate annotations for ORM mapping.
 *
 * - @Data - Generates getters, setters, equals, hashcode, and a toString methods.
 * - @Builder - Provides a build pattern for constructing a Regulation object.
 * - @NoArgsConstructor - Generates a no-arg constructor, needed for Hibernate to instantiate the object.
 * - @AllArgsConstructor - Generates a constructor with all properties as arguments.
 * - @Entity - Marks this class as an entity bean, so it can be persisted in the database.
 * - @Table - Specifies the "regulations" table in the database where records of this class will be stored.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name="regulations")

public class Regulation {
    /**
     * The unique ID of the regulation.
     * Generated automatically by the persistence provider using an identity column in the database.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    /**
     * The time at which guests can arrive at the place.
     * This field is mapped to the "arrival_time" column in the "regulations" table.
     */
    @Column(name = "arrival_time")
    private String arrivalTime;

    /**
     * The time at which guests can depart from the place.
     * This field is mapped to the "departure_time" column in the "regulations" table.
     */
    @Column(name = "departure_time")
    private String departureTime;

    /**
     * The cancellation policy for reservations at the place.
     * This field is mapped to the "cancellation_policy" column in the "regulations" table.
     */
    @Column(name = "cancellation_policy")
    private String cancellationPolity;

    /**
     * The accepted payment methods for reservations at the place.
     * This field is mapped to the "payment_methods" column in the "regulations" table.
     */
    @Column(name = "payment_methods")
    @Enumerated(EnumType.STRING)
    private PaymentMethod paymentMethod;

    /**
     * Indicates whether there is an age restriction at the place.
     * This field is mapped to the "age_restriction" column in the "regulations" table.
     */
    @Column(name = "age_restriction")
    private boolean ageRestriction;

    /**
     * Indicates whether pets are allowed at the place.
     * This field is mapped to the "pets_allowed" column in the "regulations" table.
     */
    @Column(name = "pets_allowed")
    private boolean arePetsAllowed;

    /**
     * Indicates whether events are allowed at the place.
     * This field is mapped to the "events_allowed" column in the "regulations" table.
     */
    @Column(name = "events_allowed")
    private boolean areEventsAllowed;

    /**
     * Indicates whether smoking is allowed at the place.
     * This field is mapped to the "smoking_allowed" column in the "regulations" table.
     */
    @Column(name = "smoking_allowed")
    private boolean smokingAllowed;

    /**
     * Specifies the quiet hours policy at the place.
     * This field is mapped to the "quiet_hours" column in the "regulations" table.
     */
    @Column(name = "quiet_hours")
    private String quietHours;

    /**
     * The place to which these regulations are associated.
     * Mapped as a one-to-one relationship to the Place entity, with the foreign key being the place_id column.
     * If the referenced Place entity is deleted, this Regulation entity will also be deleted due to the cascade delete setting.
     */
    @OneToOne
    @JoinColumn(name = "place_id", referencedColumnName = "id")
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JsonIgnore
    private Place place;
}
