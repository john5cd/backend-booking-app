package com.cameinw.cameinwbackend.user.service;

import com.cameinw.cameinwbackend.user.dto.MessageDTO;
import com.cameinw.cameinwbackend.user.model.Message;

import java.util.List;
import java.util.Optional;

/**
 * The MessageService interface defines methods for managing messages and handling message-related operations.
 * It is used for operations such as retrieving messages and creating messages for user communication.
 */
public interface MessageService {
        /**
         * Retrieve a list of messages for a specific user by userId.
         *
         * @param userId The unique identifier of the user.
         * @return An optional list of Message objects associated with the user, or an empty optional if none are found.
         */
        Optional<List<Message>> getMessagesPerUser(Integer userId);

        /**
         * Retrieve the chat history between two users by their userIds.
         *
         * @param userId1 The unique identifier of the first user.
         * @param userId2 The unique identifier of the second user.
         * @return An optional list of Message objects representing the chat history between the two users,
         *         or an empty optional if none are found.
         */
        Optional<List<Message>> getChatHistory(Integer userId1, Integer userId2);

        /**
         * Create a new message for user communication.
         *
         * @param senderId   The unique identifier of the message sender.
         * @param receiverId The unique identifier of the message receiver.
         * @param message    The content of the message.
         * @return The MessageDTO object representing the created message.
         */
        MessageDTO createMessage(Integer senderId, Integer receiverId, String message);


}
