package com.cameinw.cameinwbackend.user.model;

import com.cameinw.cameinwbackend.place.model.Place;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import java.util.Date;

/**
 * The Reservation class represents a user's reservation for a place.
 * Each reservation has a unique ID, check-in and check-out dates, and references to the user and place.
 *
 * The class uses Lombok library annotations for convenient boilerplate code reduction and Hibernate annotations for ORM mapping.
 *
 * - @Data - Generates getters, setters, equals, hashcode, and a toString methods.
 * - @Builder - Provides a build pattern for constructing a Reservation object.
 * - @NoArgsConstructor - Generates a no-arg constructor, needed for Hibernate to instantiate the object.
 * - @AllArgsConstructor - Generates a constructor with all properties as arguments.
 * - @Entity - Marks this class as an entity bean, so it can be persisted in the database.
 * - @Table - Specifies the "reservations" table in the database where records of this class will be stored.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name="reservations")

public class Reservation {
    /**
     * The unique ID of the reservation.
     * Generated automatically by the persistence provider using an identity column in the database.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    /**
     * The date of check-in for the reservation.
     * This field is mapped to the "check_in" column in the "reservations" table.
     */
    @Temporal(TemporalType.DATE)
    @Column(name = "check_in")
    private Date checkIn;

    /**
     * The date of check-out for the reservation.
     * This field is mapped to the "check_out" column in the "reservations" table.
     */
    @Temporal(TemporalType.DATE)
    @Column(name = "check_out")
    private Date checkOut;

    /**
     * The user who made the reservation.
     * Mapped as a many-to-one relationship to the User entity, with the foreign key being the user_id column.
     * If the referenced User entity is deleted, this Reservation entity will also be deleted due to the cascade delete setting.
     */
    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JsonIgnore
    private User user;

    /**
     * The place for which the reservation is made.
     * Mapped as a many-to-one relationship to the Place entity, with the foreign key being the place_id column.
     * If the referenced Place entity is deleted, this Reservation entity will also be deleted due to the cascade delete setting.
     */
    @ManyToOne
    @JoinColumn(name = "place_id", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JsonIgnore
    private Place place;
}
