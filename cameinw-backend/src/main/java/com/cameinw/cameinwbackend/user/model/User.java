package com.cameinw.cameinwbackend.user.model;

import com.cameinw.cameinwbackend.image.model.Image;
import com.cameinw.cameinwbackend.user.enums.Role;
import com.cameinw.cameinwbackend.place.model.Place;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

/**
 * The User class represents a user in the system.
 * Each user has a unique ID, username, email, password, name, phone number, role, and references to various related entities.
 *
 * The class implements the UserDetails interface for Spring Security authentication.
 *
 * The class uses Lombok library annotations for convenient boilerplate code reduction and Hibernate annotations for ORM mapping.
 *
 * - @Data - Generates getters, setters, equals, hashcode, and a toString methods.
 * - @Builder - Provides a build pattern for constructing a User object.
 * - @NoArgsConstructor - Generates a no-arg constructor, needed for Hibernate to instantiate the object.
 * - @AllArgsConstructor - Generates a constructor with all properties as arguments.
 * - @Entity - Marks this class as an entity bean, so it can be persisted in the database.
 * - @Table - Specifies the "users" table in the database where records of this class will be stored.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name="users")
public class User implements UserDetails{
    /**
     * The unique ID of the user.
     * Generated automatically by the persistence provider using an identity column in the database.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    /**
     * The username of the user.
     * This field is mapped to the "username" column in the "users" table.
     */
    @Column(name="username", unique=true, nullable=false)
    private String theUserName;

    /**
     * The email address of the user.
     * This field is mapped to the "email" column in the "users" table.
     */
    @Column(name="email", unique=true, nullable=false)
    private String email;

    /**
     * The password of the user.
     * This field is mapped to the "password" column in the "users" table.
     */
    @Column(name="password")
    private String password;

    /**
     * The first name of the user.
     * This field is mapped to the "first_name" column in the "users" table.
     */
    @Column(name="first_name")
    private String firstName;

    /**
     * The last name of the user.
     * This field is mapped to the "last_name" column in the "users" table.
     */
    @Column(name="last_name")
    private String lastName;

    /**
     * The phone number of the user.
     * This field is mapped to the "phone" column in the "users" table.
     */
    @Column(name="phone")
    private String phoneNumber;

    /**
     * The image name associated with the user's profile.
     * This field is mapped to the "image_name" column in the "users" table.
     */
    @Column(name="image_name")
    private String imageName = "userImg.jpg";

    /**
     * The role of the user (e.g., ADMIN, USER).
     * This field is mapped to the "role" column in the "users" table.
     */
    @Column(name="role")
    @Enumerated(EnumType.STRING)
    private Role role;

    /**
     * A collection of places associated with the user.
     * Mapped as a one-to-many relationship to the Place entity.
     * The "mappedBy" attribute refers to the "user" field in the Place entity,
     * indicating that the Place entity is the owner of the relationship.
     */
    @OneToMany(mappedBy = "user")
    private List<Place> places;

    /**
     * A collection of reservations made by the user.
     * Mapped as a one-to-many relationship to the Reservation entity.
     * The "mappedBy" attribute refers to the "user" field in the Reservation entity,
     * indicating that the Reservation entity is the owner of the relationship.
     */
    @OneToMany(mappedBy = "user")
    //@JsonIgnore
    private List<Reservation> reservations;

    /**
     * A collection of reviews submitted by the user.
     * Mapped as a one-to-many relationship to the Review entity.
     * The "mappedBy" attribute refers to the "user" field in the Review entity,
     * indicating that the Review entity is the owner of the relationship.
     */
    @OneToMany(mappedBy = "user")
    private List<Review> reviews;

    /**
     * A collection of messages sent by the user.
     * Mapped as a one-to-many relationship to the Message entity.
     * The "mappedBy" attribute refers to the "sender" field in the Message entity,
     * indicating that the Message entity is the owner of the relationship.
     */
    @OneToMany(mappedBy = "sender")
    @JsonIgnore
    private List<Message> sendedMessages;

    /**
     * A collection of messages received by the user.
     * Mapped as a one-to-many relationship to the Message entity.
     * The "mappedBy" attribute refers to the "receiver" field in the Message entity,
     * indicating that the Message entity is the owner of the relationship.
     */
    @OneToMany(mappedBy = "receiver")
    @JsonIgnore
    private List<Message> receivedMessages;

    /**
     * The image associated with the user's profile.
     * Mapped as a one-to-one relationship to the Image entity.
     * The "mappedBy" attribute refers to the "user" field in the Image entity,
     * indicating that the Image entity is the owner of the relationship.
     */
    @OneToOne(mappedBy = "user")
    private Image image;

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", theUserName='" + theUserName + '\'' +
                ", email='" + email + '\'' +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", phoneNumber='" + phoneNumber + '\'' +
                // We don't include the places, reservations, reviews, searches, sendedMessages, and receivedMessages fields here to avoid circular reference.
                // TODO: CHECK AGAIN LATER
                '}';
    }

    @Override
    public String getUsername() {
        return email;
    }
    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority(role.name()));
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
