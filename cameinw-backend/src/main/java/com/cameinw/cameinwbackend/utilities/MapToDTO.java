package com.cameinw.cameinwbackend.utilities;

import com.cameinw.cameinwbackend.user.dto.MessageDTO;
import com.cameinw.cameinwbackend.user.dto.UserDTO;
import com.cameinw.cameinwbackend.user.model.Message;
import com.cameinw.cameinwbackend.user.model.User;

/**
 * This MapToDTO class provides methods to map objects of domain models to their corresponding DTOs (Data Transfer Objects).
 */
public class MapToDTO {
    /**
     * Maps a User object to a UserDTO object.
     *
     * @param user The User object to be mapped.
     * @return A UserDTO object containing the mapped user data.
     */
    public static UserDTO mapUserToDTO(User user) {
        UserDTO userDTO = new UserDTO();
        userDTO.setUserId(user.getId());
        userDTO.setUsername(user.getTheUserName());
        userDTO.setEmail(user.getEmail());
        userDTO.setFirstName(user.getFirstName());
        userDTO.setLastName(user.getLastName());
        userDTO.setPhone(user.getPhoneNumber());

        return userDTO;
    }

    /**
     * Maps a Message object to a MessageDTO object.
     *
     * @param message The Message object to be mapped.
     * @return A MessageDTO object containing the mapped message data.
     */
    public static MessageDTO mapMessageToDTO(Message message) {
        MessageDTO messageDTO = new MessageDTO();
        messageDTO.setSenderId(message.getSender().getId());
        messageDTO.setSenderUsername(message.getSender().getTheUserName());
        messageDTO.setSenderImageName(message.getSender().getImageName());
        messageDTO.setReceiverId(message.getReceiver().getId());
        messageDTO.setReceiverUsername(message.getReceiver().getTheUserName());
        messageDTO.setReceiverImageName(message.getReceiver().getImageName());
        messageDTO.setMessageTimestamp(message.getTimestamp());
        messageDTO.setMessage(message.getMessage());
        return messageDTO;
    }
}
