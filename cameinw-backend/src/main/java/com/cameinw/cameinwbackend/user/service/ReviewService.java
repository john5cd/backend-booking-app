package com.cameinw.cameinwbackend.user.service;

import com.cameinw.cameinwbackend.user.dto.UserDTO;
import com.cameinw.cameinwbackend.user.model.Review;
import com.cameinw.cameinwbackend.user.request.ReviewRequest;

import java.util.List;

/**
 * The ReviewService interface defines methods for managing reviews related to places.
 * It is used for operations such as retrieving, creating, updating, and deleting reviews.
 */
public interface ReviewService {
    /**
     * Retrieve a list of all reviews.
     *
     * @return A list of Review objects.
     */
    List<Review> getAllReviews();

    /**
     * Retrieves the user who made a specific review by review ID.
     *
     * @param reviewId The ID of the review for which to find the user.
     * @return A UserDTO object containing the user data if found, or null if not found.
     */
    UserDTO getUserByReviewId(Integer reviewId);

    /**
     * Create a new review for a place.
     *
     * @param placeId       The unique identifier of the place.
     * @param reviewRequest The request object containing review information.
     * @return The newly created Review object.
     */
    Review createReview(Integer placeId, ReviewRequest reviewRequest);

    /**
     * Update an existing review for a user.
     *
     * @param userId       The unique identifier of the user.
     * @param reviewId     The unique identifier of the review to update.
     * @param reviewRequest The request object containing updated review information.
     * @return The updated Review object.
     */
    Review updateReview(Integer userId, Integer reviewId, ReviewRequest reviewRequest);

    /**
     * Delete a review associated with a user by userId and reviewId.
     *
     * @param userId   The unique identifier of the user.
     * @param reviewId The unique identifier of the review to delete.
     */
    void deleteReview(Integer userId, Integer reviewId);

    /**
     * Retrieve a list of reviews associated with a user by userId.
     *
     * @param userId The unique identifier of the user.
     * @return A list of Review objects associated with the user.
     */
    List<Review> getReviewsByUserId(Integer userId);

    /**
     * Retrieve a specific review associated with a user by userId and reviewId.
     *
     * @param userId   The unique identifier of the user.
     * @param reviewId The unique identifier of the review.
     * @return The Review object associated with the user and reviewId.
     */
    Review getReviewByUserId(Integer userId, Integer reviewId);

    /**
     * Retrieve a list of reviews associated with a place by placeId.
     *
     * @param placeId The unique identifier of the place.
     * @return A list of Review objects associated with the place.
     */
    List<Review> getReviewsByPlaceId(Integer placeId);


    /**
     * Retrieve a specific review associated with a place by placeId and reviewId.
     *
     * @param placeId   The unique identifier of the place.
     * @param reviewId The unique identifier of the review.
     * @return The Review object associated with the place and reviewId.
     */
    Review getReviewByPlaceId(Integer placeId, Integer reviewId);
}
