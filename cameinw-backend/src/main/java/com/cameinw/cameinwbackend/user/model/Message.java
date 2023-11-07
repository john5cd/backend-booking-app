package com.cameinw.cameinwbackend.user.model;

import com.cameinw.cameinwbackend.utilities.Audit;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.time.LocalDateTime;

/**
 * The Message class represents a message sent between users.
 * Each message has a unique id, a message content, a timestamp and references to the sender and receiver users.
 *
 * The class uses Lombok library annotations for convenient boilerplate code reduction and Hibernate annotations for ORM mapping.
 *
 * - @Data - Generates getters, setters, equals, hashcode, and a toString methods.
 * - @Builder - Provides a build pattern for constructing a Message object.
 * - @NoArgsConstructor - Generates a no-arg constructor, needed for Hibernate to instantiate the object.
 * - @AllArgsConstructor - Generates a constructor with all properties as arguments.
 * - @Entity - Marks this class as an entity bean, so it can be persisted in the database.
 * - @Table - Specifies the "message" table in the database where records of this class will be stored.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
@Table(name="messages")
public class Message {
    /**
     * The unique ID of the message.
     * Generated automatically by the persistence provider using an identity column in the database.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    /**
     * The content of the message.
     * This field is mapped to the "message" column in the "messages" table.
     */
    @Column(name = "message")
    private String message;

    /**
     * The user who sent the message.
     * Mapped as a many-to-one relationship to the User entity, with the foreign key being the sender_id column,
     * which cannot be null. If the referenced User entity is deleted, this Message entity will also be
     * deleted due to the cascade delete setting.
     */
    @ManyToOne
    @JoinColumn(name = "sender_id", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JsonIgnore
    private User sender;

    /**
     * The user who received the message.
     * Mapped as a many-to-one relationship to the User entity, with the foreign key being the receiver_id column,
     * which cannot be null. If the referenced User entity is deleted, this Message entity will also be
     * deleted due to the cascade delete setting.
     */
    @ManyToOne
    @JoinColumn(name = "receiver_id", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JsonIgnore
    private User receiver;

    /**
     * The timestamp representing when the message was sent.
     * This field stores the date and time when the message was sent, using the format "yyyy-MM-dd HH:mm:ss".
     */
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime timestamp;

    @Override
    public String toString() {
        return "Message{" +
                "id=" + id +
                ", message='" + message + '\'' +
                ", timestamp=" + timestamp +
                ", sender=" + sender +
                ", receiver=" + receiver +
                '}';
    }
}
