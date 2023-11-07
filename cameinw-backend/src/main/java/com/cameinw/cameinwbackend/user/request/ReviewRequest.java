package com.cameinw.cameinwbackend.user.request;

import com.cameinw.cameinwbackend.user.enums.PropertyRating;
import com.cameinw.cameinwbackend.user.model.User;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Arrays;
import java.util.stream.Collectors;

/**
 * The ReviewRequest class represents a request object for creating or updating a review.
 * It includes properties such as rating, comment, and the user associated with the review.
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ReviewRequest {
    /**
     * The rating given to the property in the review.
     */
    @NotNull(message = "Rating number cannot be negative.")
    private PropertyRating rating;

    /**
     * The comment or feedback provided in the review.
     */
    private String comment;

    /**
     * The user who create the review for a specific place.
     */
    @NotNull(message = "User cannot be null.")
    private User user;

    /**
     * Validates the review request to ensure it contains valid data.
     * Throws an exception if the rating is not within the valid range.
     *
     * @throws IllegalArgumentException if the rating is not a valid property rating.
     */
    public void validate() {
        if (!isValidRating(rating)) {
            throw new IllegalArgumentException("Invalid score. Available property ratings are: " + getPropertyRatingList());        }
    }

    /**
     * Checks if a given rating is valid by verifying if it exists in the list of available property ratings.
     *
     * @param propertyRating The rating to validate.
     * @return True if the rating is valid; otherwise, false.
     */
    private boolean isValidRating(PropertyRating propertyRating) {
        return Arrays.asList(PropertyRating.values()).contains(propertyRating);
    }

    /**
     * Returns a comma-separated string of available property ratings.
     *
     * @return A string listing the available property ratings.
     */
    private String getPropertyRatingList() {
        return Arrays.stream(PropertyRating.values())
                .map(Enum::name)
                .collect(Collectors.joining(", "));
    }
}


// ------------------------ EXAMPLES -----------------------

//           --Create Review--
//
//{
//    "rating": "FIVE_STARS",
//    "user": {
//        "id": 2
//            }
//}
//           --Update Review--
//
//{
//    "comment": "Excellent apartment!"
//}
