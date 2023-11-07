package com.cameinw.cameinwbackend.user.controller;

import com.cameinw.cameinwbackend.exception.CustomUserFriendlyException;
import com.cameinw.cameinwbackend.exception.ResourceNotFoundException;
import com.cameinw.cameinwbackend.place.model.Place;
import com.cameinw.cameinwbackend.place.model.Regulation;
import com.cameinw.cameinwbackend.response.GenericResponse;
import com.cameinw.cameinwbackend.user.dto.UserDTO;
import com.cameinw.cameinwbackend.user.model.Review;
import com.cameinw.cameinwbackend.user.model.User;
import com.cameinw.cameinwbackend.user.request.ReviewRequest;
import com.cameinw.cameinwbackend.user.service.ReviewService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * The ReviewController class defines RESTful endpoints for managing and retrieving user reviews.
 *
 * This class is annotated with:
 * - @RestController: Marks this class as a REST controller, allowing it to handle HTTP requests.
 * - @RequestMapping("/api"): Specifies the base URL path for all endpoints in this controller.
 * - @RequiredArgsConstructor: automatically generates a constructor for this class that injects the final fields declared in the class.
 */
@RequiredArgsConstructor
@RestController
@RequestMapping("/api")
public class ReviewController {
    /**
     * The ReviewService that provides methods for managing and retrieving user reviews.
     */
    private final ReviewService reviewService;

    /**
     * Retrieves a list of all reviews.
     *
     * @return A ResponseEntity containing the list of reviews if found, or an error response if not found.
     * @throws ResourceNotFoundException if no reviews are found.
     */
    @GetMapping("/reviews") // CHECK OK!!
    public ResponseEntity<Object>  getAllReviews() {
        GenericResponse genericResponse = new GenericResponse();
        try {
            List<Review> reviews = reviewService.getAllReviews();
            return new ResponseEntity<>(reviews, HttpStatus.OK);
        } catch (ResourceNotFoundException ex) {
            genericResponse.setMessage(ex.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(genericResponse);
        }
    }

    /**
     * Retrieves the user who made a specific review by review ID.
     *
     * @param reviewId The ID of the review for which to find the user.
     * @return A ResponseEntity containing the user data if found, or an error response if not found.
     * @throws ResourceNotFoundException if the review or user is not found.
     */
    @GetMapping("/reviews/{review_id}/user")
    public ResponseEntity<Object> getUserByReviewId(@PathVariable("review_id") Integer reviewId) {
        GenericResponse genericResponse = new GenericResponse();
        try {
            UserDTO UserDTO = reviewService.getUserByReviewId(reviewId);
            return ResponseEntity.status(HttpStatus.OK).body(UserDTO);
        } catch (ResourceNotFoundException ex) {
            genericResponse.setMessage(ex.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(genericResponse);
        }
    }

    /**
     * Creates a new review for a place.
     *
     * @param placeId The ID of the place for which the review is being created.
     * @param reviewRequest The ReviewRequest object containing the review data.
     * @return A ResponseEntity with a success message or an error response if not found or an error occurs.
     * @throws ResourceNotFoundException if the place is not found.
     * @throws CustomUserFriendlyException if there is a validation error or user-friendly exception.
     */
    @PostMapping("/places/{place_id}/reviews") // !!CHECK OK!!
    public ResponseEntity<Object> createReview(
            @PathVariable("place_id") Integer placeId,
            @Valid  @RequestBody ReviewRequest reviewRequest) {
        GenericResponse genericResponse = new GenericResponse();
        try {
            Review createReview = reviewService.createReview(placeId, reviewRequest);
            genericResponse.setMessage("Review successfully created.");
            return ResponseEntity.status(HttpStatus.CREATED).body(genericResponse);
        } catch (ResourceNotFoundException ex) {
            genericResponse.setMessage(ex.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(genericResponse);
        } catch (CustomUserFriendlyException ex) {
            genericResponse.setMessage(ex.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(genericResponse);
        }
    }

    /**
     * Updates an existing review.
     *
     * @param userId The ID of the user who made the review.
     * @param reviewId The ID of the review to update.
     * @param reviewRequest The ReviewRequest object containing the updated review data.
     * @return A ResponseEntity with a success message or an error response if not found or an error occurs.
     * @throws ResourceNotFoundException if the review or user is not found.
     */
    @PutMapping("/users/{user_id}/reviews/{review_id}")
    public ResponseEntity<Object> updateReview(
            @PathVariable("user_id") Integer userId,
            @PathVariable("review_id") Integer reviewId,
            @RequestBody ReviewRequest reviewRequest) {
        GenericResponse genericResponse = new GenericResponse();
        try {
            Review updateReview = reviewService.updateReview(userId, reviewId, reviewRequest);
            genericResponse.setMessage("Review successfully updated.");
            return ResponseEntity.status(HttpStatus.OK).body(genericResponse);
        } catch (ResourceNotFoundException ex) {
            genericResponse.setMessage(ex.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(genericResponse);
        } catch (Exception ex) {
            genericResponse.setMessage(ex.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(genericResponse);
        }
    }

    /**
     * Deletes a review made by a specific user.
     *
     * @param userId The ID of the user who made the review.
     * @param reviewId The ID of the review to delete.
     * @return A ResponseEntity with a success message or an error response if not found.
     * @throws ResourceNotFoundException if the review or user is not found.
     * @throws CustomUserFriendlyException if the user does not have permission to delete the review.
     */
    @DeleteMapping("/users/{user_id}/reviews/{review_id}")
    public ResponseEntity<Object> deleteReview(
            @PathVariable("user_id") Integer userId,
            @PathVariable("review_id") Integer reviewId) {
        GenericResponse genericResponse = new GenericResponse();
        try {
            reviewService.deleteReview(userId, reviewId);
            genericResponse.setMessage("Review deleted successfully.");
            return ResponseEntity.status(HttpStatus.NO_CONTENT).body(genericResponse);
        } catch (ResourceNotFoundException ex) {
            genericResponse.setMessage(ex.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(genericResponse);
        } catch (CustomUserFriendlyException ex) {
            genericResponse.setMessage(ex.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(genericResponse);
        }
    }

    /**
     * Retrieves reviews made by a specific user.
     *
     * @param userId The ID of the user.
     * @return A ResponseEntity containing the list of reviews made by the user if found, or an error response if not found.
     * @throws ResourceNotFoundException if the user or reviews are not found.
     */
    @GetMapping("/users/{user_id}/reviews")  // -----------
    public ResponseEntity<Object> getReviewsByUserId(@PathVariable("user_id") Integer userId) {
        GenericResponse genericResponse = new GenericResponse();
        try {
            List<Review> reviews = reviewService.getReviewsByUserId(userId);
            return ResponseEntity.status(HttpStatus.OK).body(reviews);
        } catch (ResourceNotFoundException ex) {
            genericResponse.setMessage(ex.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(genericResponse);
        }
    }

    /**
     * Retrieves a specific review made by a user.
     *
     * @param userId The ID of the user who made the review.
     * @param reviewId The ID of the review to retrieve.
     * @return A ResponseEntity containing the review data if found, or an error response if not found.
     * @throws ResourceNotFoundException if the review or user is not found.
     */
    @GetMapping("/users/{user_id}/reviews/{review_id}")  // -----------
    public ResponseEntity<Object> getReviewByUserId(
            @PathVariable("user_id") Integer userId,
            @PathVariable("review_id") Integer reviewId) {
        GenericResponse genericResponse = new GenericResponse();
        try {
            Review review = reviewService.getReviewByUserId(userId, reviewId);
            return ResponseEntity.status(HttpStatus.OK).body(review);
        } catch (ResourceNotFoundException ex) {
            genericResponse.setMessage(ex.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(genericResponse);
        }
    }


    /**
     * Retrieves reviews for a specific place.
     *
     * @param placeId The ID of the place.
     * @return A ResponseEntity containing the list of reviews for the place if found, or an error response if not found.
     * @throws ResourceNotFoundException if the place or reviews are not found.
     */
    @GetMapping("/places/{place_id}/reviews")  // !!CHECK OK!!
    public ResponseEntity<Object> getReviewsByPlaceId(@PathVariable("place_id") Integer placeId) {
        GenericResponse genericResponse = new GenericResponse();
        try {
            List<Review> reviews = reviewService.getReviewsByPlaceId(placeId);
            return ResponseEntity.status(HttpStatus.OK).body(reviews);
        } catch (ResourceNotFoundException ex) {
            genericResponse.setMessage(ex.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(genericResponse);
        }
    }

    /**
     * Retrieves a specific review for a place.
     *
     * @param placeId The ID of the place.
     * @param reviewId The ID of the review to retrieve.
     * @return A ResponseEntity containing the review data if found, or an error response if not found.
     * @throws ResourceNotFoundException if the review or place is not found.
     */
    @GetMapping("/places/{place_id}/reviews/{review_id}") // !!CHECK OK!!
    public ResponseEntity<Object> getReviewByPlaceId(
            @PathVariable("place_id") Integer placeId,
            @PathVariable("review_id") Integer reviewId) {
        GenericResponse genericResponse = new GenericResponse();
        try {
            Review review = reviewService.getReviewByPlaceId(placeId, reviewId);
            return ResponseEntity.status(HttpStatus.OK).body(review);
        } catch (ResourceNotFoundException ex) {
            genericResponse.setMessage(ex.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(genericResponse);
        }
    }

}
