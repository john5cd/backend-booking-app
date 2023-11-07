package com.cameinw.cameinwbackend.place.request;

import com.cameinw.cameinwbackend.place.enums.PaymentMethod;
import com.cameinw.cameinwbackend.place.model.Place;
import com.cameinw.cameinwbackend.user.model.User;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Arrays;
import java.util.stream.Collectors;

/**
 * The RegulationRequest class represents a request object for specifying regulations related to a place.
 * It includes properties such as arrival time, departure time, payment method, and various other regulations.
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RegulationRequest {
    /**
     * The arrival time regulation for the place.
     */
    @NotBlank(message = "Arrival time cannot be null or empty.")
    private String arrivalTime;

    /**
     * The departure time regulation for the place.
     */
    @NotBlank(message = "Departure time cannot be null or empty.")
    private String departureTime;

    /**
     * The cancellation policy regulation for the place.
     */
    private String cancellationPolicy;

    /**
     * The payment method regulation for the place.
     */
    @NotNull(message = "Payment method cannot be null.")
    private PaymentMethod paymentMethod;

    /**
     * Indicates whether there is an age restriction for the place.
     */
    private boolean ageRestriction;

    /**
     * Indicates whether pets are allowed at the place.
     */
    private boolean arePetsAllowed;

    /**
     * Indicates whether events are allowed at the place.
     */
    private boolean areEventsAllowed;

    /**
     * Indicates whether smoking is allowed at the place.
     */
    private boolean smokingAllowed;

    /**
     * The quiet hours regulation for the place.
     */
    private String quietHours;

    /**
     * The user who defines these regulations for their place.
     */
    @NotNull(message = "User cannot be null.")
    private User user;


    /**
     * Validates the RegulationRequest object, ensuring that the payment method is valid.
     *
     * @throws IllegalArgumentException if the payment method is not valid.
     */
    public void validate() {
        if (!isValidPaymentMethode(paymentMethod)) {
            throw new IllegalArgumentException("Invalid payment method. Available payment methods are: " + getPaymentMethodsList());        }
    }

    /**
     * Checks if the provided payment method is valid.
     *
     * @param paymentMethod The payment method to check.
     * @return true if the payment method is valid, false otherwise.
     */
    private boolean isValidPaymentMethode(PaymentMethod paymentMethod) {
        return Arrays.asList(PaymentMethod.values()).contains(paymentMethod);
    }

    /**
     * Generates a comma-separated list of available payment methods.
     *
     * @return A string containing available payment methods.
     */
    private String getPaymentMethodsList() {
        return Arrays.stream(PaymentMethod.values())
                .map(Enum::name)
                .collect(Collectors.joining(", "));
    }
}

// ------------------------ EXAMPLES -----------------------

//           --Create Regulation--
//
//{
//    "arrivalTime": "13:00",
//    "departureTime": "11:00",
//    "paymentMethod": "CASH_AND_CARD",
//    "user" : {
//        "id" : 2
//            }
//}


//           --Update Regulation--
//
//{
//    "arrivalTime": "13:00",
//    "cancellationPolicy": "Reservations cannot be cancelled",
//    "departureTime": "11:00",
//    "paymentMethod": "CASH_AND_CARD",
//    "user" : {
//        "id" : 2
//            }
//}
