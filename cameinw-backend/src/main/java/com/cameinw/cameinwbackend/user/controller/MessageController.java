package com.cameinw.cameinwbackend.user.controller;

import com.cameinw.cameinwbackend.exception.ResourceNotFoundException;
import com.cameinw.cameinwbackend.user.dto.MessageDTO;
import com.cameinw.cameinwbackend.user.model.Message;
import com.cameinw.cameinwbackend.user.service.MessageService;
import com.cameinw.cameinwbackend.utilities.MapToDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * The MessageController class defines RESTful endpoints for managing and retrieving user messages.
 *
 * This class is annotated with:
 * - @RestController: Marks this class as a REST controller, allowing it to handle HTTP requests.
 * - @RequestMapping("/api"): Specifies the base URL path for all endpoints in this controller.
 * - @RequiredArgsConstructor: automatically generates a constructor for this class that injects the final fields declared in the class.
 */
@RequiredArgsConstructor
@RestController
@RequestMapping("/api")
public class MessageController {
    /**
     * The MessageService that provides methods for managing and retrieving user messages.
     */
    private final MessageService messageService;

    /**
     * Retrieves messages sent and received by a specific user.
     *
     * @param userId The ID of the user for whom messages are retrieved.
     * @return A ResponseEntity containing a list of MessageDTO objects if found, or an error response if not found.
     */
    @GetMapping("/users/{user_id}/messages")
    public ResponseEntity<List<MessageDTO>> getUsersMessages(@PathVariable("user_id") Integer userId) {
        Optional<List<Message>> messages = messageService.getMessagesPerUser(userId);
        if (messages.isPresent()) {
            List<MessageDTO> messageDTOs = messages.get().stream()
                    .map(MapToDTO::mapMessageToDTO)  // Map Message to MessageDTO
                    .collect(Collectors.toList());
            return ResponseEntity.ok(messageDTOs);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * Retrieves the chat history between two users.
     *
     * @param userId The ID of the first user.
     * @param otherUserId The ID of the other user in the chat.
     * @return A ResponseEntity containing a list of MessageDTO objects representing the chat history if found,
     *         or an error response if not found.
     */
    @GetMapping("/users/{user_id}/messages/{otherUser_id}")
    public ResponseEntity<List<MessageDTO>> getUsersChat(
            @PathVariable("user_id") Integer userId,
            @PathVariable("otherUser_id") Integer otherUserId
    ) {
        Optional<List<Message>> chatHistory = messageService.getChatHistory(userId, otherUserId);
        if (chatHistory.isPresent()) {

            List<MessageDTO> messageDTOs = chatHistory.get().stream()
                    .map(MapToDTO::mapMessageToDTO)  // Map Message to MessageDTO
                    .collect(Collectors.toList());
            return ResponseEntity.ok(messageDTOs);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * Creates a new message in a chat between two users.
     *
     * @param userId The ID of the user sending the message.
     * @param otherUserId The ID of the other user in the chat.
     * @param messageText The text of the message to be created.
     * @return A ResponseEntity containing a MessageDTO representing the created message if successful,
     * or an error response if not found or an error occurs.
     */
    @PostMapping("/users/{user_id}/messages/{otherUser_id}")
    public ResponseEntity<MessageDTO> createMessage(
            @PathVariable("user_id") Integer userId,
            @PathVariable("otherUser_id") Integer otherUserId,
            @RequestParam("message") String messageText) {
        try {
            MessageDTO newMessage = messageService.createMessage(userId, otherUserId, messageText);
            return ResponseEntity.ok(newMessage);
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

}
