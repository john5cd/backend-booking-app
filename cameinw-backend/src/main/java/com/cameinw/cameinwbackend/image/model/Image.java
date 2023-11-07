package com.cameinw.cameinwbackend.image.model;

import com.cameinw.cameinwbackend.place.model.Place;
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
 * The Image class represents an image associated with a certain place.
 * Each image has a unique id, an image name, and a reference to the place it pertains to.
 *
 * The class uses Lombok library annotations for convenient boilerplate code reduction and Hibernate annotations for ORM mapping.
 *
 * - @Data - Generates getters, setters, equals, hashcode, and a toString methods.
 * - @Builder - Provides a build pattern for constructing an Image object.
 * - @NoArgsConstructor - Generates a no-arg constructor, needed for Hibernate to instantiate the object.
 * - @AllArgsConstructor - Generates a constructor with all properties as arguments.
 * - @Entity - Marks this class as an entity bean, so it can be persisted in the database.
 * - @Table - Specifies the "images" table in the database where records of this class will be stored.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name="images")

public class Image {
    /**
     * The unique ID of the image.
     * Generated automatically by the persistence provider using an identity column in the database.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    /**
     * The name of the image file.
     * This field is mapped to the "image_name" column in the "images" table.
     */
    @Column(name = "image_name")
    private String imageName;

    /**
     * The place to which this image is associated.
     * Mapped as a many-to-one relationship to the Place entity, with the foreign key being the place_id column,
     * which cannot be null. If the referenced Place entity is deleted, this Image entity will also be
     * deleted due to the cascade delete setting.
     */
    @ManyToOne
    @JoinColumn(name = "place_id", nullable = true)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JsonIgnore
    private Place place;

    @OneToOne
    @JoinColumn(name = "userId", unique = true)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private User user;
}
