package com.cameinw.cameinwbackend.user.repository;

import com.cameinw.cameinwbackend.user.model.Message;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * The MessageRepository interface provides methods to interact with the database for Message entities.
 * It extends the JpaRepository interface, which provides basic CRUD operations for Message entities.
 *
 * This repository interface is annotated with @Repository to indicate that it is a Spring Data repository,
 * allowing it to be automatically discovered by Spring's component scanning and used for database operations.
 */
@Repository
public interface MessageRepository extends JpaRepository<Message, Integer> {
    /**
     * Find messages for a specific user, returning only the most recent message in each conversation.
     *
     * @param userId The unique identifier of the user.
     * @return A list of messages representing the most recent message in each conversation for the user.
     */
    @Query(value = "SELECT m1.* "
            + "FROM messages m1 "
            + "LEFT JOIN messages m2 ON ((m1.sender_id = m2.sender_id AND m1.receiver_id = m2.receiver_id) OR (m1.sender_id = m2.receiver_id AND m1.receiver_id = m2.sender_id)) "
            + "AND m1.timestamp < m2.timestamp "
            + "WHERE (m2.id IS NULL) AND (m1.sender_id = :userId OR m1.receiver_id = :userId) "
            + "ORDER BY m1.timestamp DESC", nativeQuery = true)
    List<Message> findMessagesPerUser(@Param("userId") Integer userId);

    /**
     * Find messages between two users, ordered by timestamp in ascending order.
     *
     * @param user1Id The unique identifier of the first user.
     * @param user2Id The unique identifier of the second user.
     * @return A list of messages exchanged between the two users, ordered by timestamp in ascending order.
     */
    @Query("SELECT m FROM Message m " +
            "WHERE (m.sender.id = :user1Id AND m.receiver.id = :user2Id) OR (m.sender.id = :user2Id AND m.receiver.id = :user1Id) " +
            "ORDER BY m.timestamp ASC")
    List<Message> findMessagesBetweenUsers(@Param("user1Id") Integer user1Id, @Param("user2Id") Integer user2Id);
}
