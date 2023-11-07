package com.cameinw.cameinwbackend.user.request;

import com.cameinw.cameinwbackend.user.model.User;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * The ReservationRequest class represents a request object for creating a reservation.
 * It includes properties such as check-in date, check-out date, and the user associated with the reservation.
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ReservationRequest {
    /**
     * The check-in date for the reservation.
     */
    @NotNull(message = "Check in cannot be null.")
    private Date checkIn;

    /**
     * The check-out date for the reservation.
     */
    @NotNull(message = "Check out cannot be null.")
    private Date checkOut;

    /**
     * The user who make the reservation.
     */
    @NotNull(message = "User cannot be null.")
    User user;
}

// ------------------------ EXAMPLE -----------------------

//           --Create Reservation--
//
//{
//    "checkIn": "2023-09-10",
//    "checkOut": "2023-09-15",
//    "user": {
//        "id": 2
//            }
//}
