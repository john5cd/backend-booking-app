package com.cameinw.cameinwbackend.user.model;

import com.cameinw.cameinwbackend.utilities.Audit;
import com.cameinw.cameinwbackend.place.model.Place;
import com.cameinw.cameinwbackend.user.enums.PropertyRating;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.OnDelete;

import org.hibernate.annotations.OnDeleteAction;

/**
 * The Review class represents an enums's rating and review for a specific place.
 * Each rating has a unique id, an enum rating value, a comment, and references to the place and users it pertains to.
 *
 * The class uses Lombok library annotations for convenient boilerplate code reduction and Hibernate annotations for ORM mapping.
 *
 * -@Data - Generates getters, setters, equals, hashcode, and a toString methods.
 * -@Builder - Provides a build pattern for constructing a Rating object.
 * -@NoArgsConstructor - Generates a no-arg constructor, needed for Hibernate to instantiate the object.
 * -@AllArgsConstructor - Generates a constructor with all properties as arguments.
 * -@Entity - Marks this class as an entity bean, so it can be persisted in the database.
 * -@Table - Specifies the "ratings" table in the database where records of this class will be stored.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name="reviews")
public class Review extends Audit {
    /**
     * The unique ID of the rating.
     * Generated automatically by the persistence provider using an identity column in the database.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    /**
     * The rating field represents the rating value for a review.
     * It is mapped to the "rating" column in the "reviews" table.
     */
    @Column(name = "rating")
    @Enumerated(EnumType.STRING)
    private PropertyRating rating;


    /**
     * The comment or review provided by the enums for the place.
     * This field is mapped to the "comment" column in the "ratings" table.
     */
    @Column(name = "comment")
    private String comment;

    /**
     * The place to which this review is associated.
     * Mapped as a many-to-one relationship to the Place entity, with the foreign key being the place_id column,
     * which cannot be null. If the referenced Place entity is deleted, this Review entity will also be
     * deleted due to the cascade delete setting.
     */
    @ManyToOne
    @JoinColumn(name = "place_id", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JsonIgnore
    private Place place;

    /**
     * The users who provided the rating and review.
     * Mapped as a many-to-one relationship to the User entity, with the foreign key being the user_id column,
     * which cannot be null. If the referenced User entity is deleted, this Review entity will also be
     * deleted due to the cascade delete setting.
     */
    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JsonIgnore
    private User user;
}
