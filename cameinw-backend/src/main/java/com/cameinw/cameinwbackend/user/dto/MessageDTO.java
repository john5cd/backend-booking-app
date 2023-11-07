package com.cameinw.cameinwbackend.user.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * A Data Transfer Object (DTO) representing a message sent between users.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MessageDTO {
    /**
     * The ID of the user who sent the message.
     */
    private Integer senderId;

    /**
     * The username of the user who sent the message.
     */
    private String senderUsername;

    /**
     * The name of the image associated with the sender's profile.
     */
    private String senderImageName;

    /**
     * The ID of the user who received the message.
     */
    private Integer receiverId;

    /**
     * The username of the user who received the message.
     */
    private String receiverUsername;

    /**
     * The name of the image associated with the receiver's profile.
     */
    private String receiverImageName;

    /**
     * The content of the message.
     */
    private String message;

    /**
     * The timestamp when the message was sent.
     */
    private LocalDateTime messageTimestamp;
}
