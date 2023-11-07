package com.cameinw.cameinwbackend.place.request;

import com.cameinw.cameinwbackend.place.model.Place;
import com.cameinw.cameinwbackend.user.model.User;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * The FacilityRequest class represents a request object for specifying facility-related details of a place.
 * It includes properties such as free parking, smoking policy, Wi-Fi availability, breakfast, balcony, swimming pool,
 * and the user associated with these facilities.
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class FacilityRequest {
    /**
     * Indicates whether the place has free parking.
     */
    private boolean hasFreeParking;

    /**
     * Indicates whether the place is non-smoking.
     */
    private boolean isNonSmoking;

    /**
     * Indicates whether the place has free Wi-Fi.
     */
    private boolean hasFreeWiFi;

    /**
     * Indicates whether breakfast is provided at the place.
     */
    @NotNull(message = "Breakfast cannot be null.")
    private Boolean hasbreakfast;

    /**
     * Indicates whether the place has a balcony.
     */
    private boolean hasbalcony;

    /**
     * Indicates whether the place has a swimming pool.
     */
    private boolean hasSwimmingPool;

    /**
     * The user who defines these facility details for their place.
     */
    @NotNull(message = "User cannot be null.")
    private User user;
}

// ------------------------ EXAMPLES -----------------------

//           --Create Facility--
//
//{
//    "hasbreakfast": true,
//    "user" : {
//        "id" : 2
//    }
//}

//           --Update Facility--
//
//{
//    "hasFreeWiFi" : true,
//    "hasbreakfast": true,
//    "user" : {
//        "id" : 2
//    }
//}
