package com.cameinw.cameinwbackend.user.service.implementation;

import com.cameinw.cameinwbackend.exception.ResourceNotFoundException;
import com.cameinw.cameinwbackend.user.dto.MessageDTO;
import com.cameinw.cameinwbackend.user.model.Message;
import com.cameinw.cameinwbackend.user.model.User;
import com.cameinw.cameinwbackend.user.repository.MessageRepository;
import com.cameinw.cameinwbackend.user.repository.UserRepository;
import com.cameinw.cameinwbackend.user.service.MessageService;
import com.cameinw.cameinwbackend.utilities.MapToDTO;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

/**
 * The MessageServiceImpl class implements the MessageService interface and provides methods for managing messages
 * and handling message-related operations.
 *
 * This service class is annotated with @Service to indicate that it is a Spring service component,
 * allowing it to be automatically discovered by Spring's component scanning and dependency injection mechanism.
 *
 * The @RequiredArgsConstructor annotation is used here to automatically generate a constructor for this class
 * that initializes the final fields marked with the 'final' keyword. This constructor simplifies the injection
 * of dependencies required by this service, such as MessageRepository and UserRepository.
 */
@RequiredArgsConstructor
@Service
public class MessageServiceImpl implements MessageService {
    /**
     * The MessageRepository responsible for database operations related to messages.
     */
    private final MessageRepository messageRepository;
    /**
     * The UserRepository responsible for database operations related to users.
     */
    private final UserRepository userRepository;

    /**
     * Retrieves a list of messages sent by or to a specific user.
     *
     * @param userId The ID of the user.
     * @return An optional list of Message entities if the user is found, otherwise empty.
     */
    @Override
    public Optional<List<Message>> getMessagesPerUser(Integer userId) {
        return userRepository.findById(userId)
                .map(user -> messageRepository.findMessagesPerUser(userId));
    }

    /**
     * Retrieves the chat history between two users.
     *
     * @param userId1 The ID of the first user.
     * @param userId2 The ID of the second user.
     * @return An optional list of Message entities representing the chat history if both users are found, otherwise empty.
     */
    @Override
    public Optional<List<Message>> getChatHistory(Integer userId1, Integer userId2) {
        return userRepository.findById(userId1).flatMap(user1 ->
                userRepository.findById(userId2).map(user2 ->
                        messageRepository.findMessagesBetweenUsers(user1.getId(), user2.getId())));
    }

    /**
     * Creates a new message and saves it to the database.
     *
     * @param senderId The ID of the message sender.
     * @param receiverId The ID of the message receiver.
     * @param message The content of the message.
     * @return A MessageDTO representing the created message.
     * @throws ResourceNotFoundException if the sender or receiver with the specified IDs is not found.
     */
    @Override
    @Transactional
    public MessageDTO createMessage(Integer senderId, Integer receiverId, String message) {
        User sender = userRepository.findById(senderId)
                .orElseThrow(() -> new ResourceNotFoundException("Sender with id " + senderId + " not found"));

        User receiver = userRepository.findById(receiverId)
                .orElseThrow(() -> new ResourceNotFoundException("Receiver with id " + receiverId + " not found"));

        Message newMessage = new Message();
        newMessage.setSender(sender);
        newMessage.setReceiver(receiver);
        newMessage.setMessage(message);

        // Set the timestamp to the current date and time
        newMessage.setTimestamp(LocalDateTime.now());

        Message savedMessage = messageRepository.save(newMessage);
        return MapToDTO.mapMessageToDTO(savedMessage);
    }
}
