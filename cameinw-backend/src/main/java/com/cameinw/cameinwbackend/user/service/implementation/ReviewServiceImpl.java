package com.cameinw.cameinwbackend.user.service.implementation;

import com.cameinw.cameinwbackend.exception.CustomUserFriendlyException;
import com.cameinw.cameinwbackend.exception.ResourceNotFoundException;
import com.cameinw.cameinwbackend.place.model.Place;
import com.cameinw.cameinwbackend.place.repository.PlaceRepository;
import com.cameinw.cameinwbackend.user.dto.UserDTO;
import com.cameinw.cameinwbackend.user.model.Reservation;
import com.cameinw.cameinwbackend.user.model.Review;
import com.cameinw.cameinwbackend.user.model.User;
import com.cameinw.cameinwbackend.user.repository.ReservationRepository;
import com.cameinw.cameinwbackend.user.repository.ReviewRepository;
import com.cameinw.cameinwbackend.user.repository.UserRepository;
import com.cameinw.cameinwbackend.user.request.ReviewRequest;
import com.cameinw.cameinwbackend.user.service.ReviewService;
import com.cameinw.cameinwbackend.utilities.MapToDTO;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Service;

/**
 * The ReviewServiceImpl class implements the ReviewService interface and provides methods for managing reviews
 * and handling review-related operations.
 *
 * This service class is annotated with @Service to indicate that it is a Spring service component,
 * allowing it to be automatically discovered by Spring's component scanning and dependency injection mechanism.
 *
 * The @RequiredArgsConstructor annotation is used here to automatically generate a constructor for this class
 * that initializes the final fields marked with the 'final' keyword. This constructor simplifies the injection
 * of dependencies required by this service, such as ReviewRepository, UserRepository, PlaceRepository, and ReservationRepository.
 */
@RequiredArgsConstructor
@Service
public class ReviewServiceImpl implements ReviewService {
    /**
     * The ReviewRepository responsible for database operations related to reviews.
     */
    private final ReviewRepository reviewRepository;
    /**
     * The UserRepository responsible for database operations related to users.
     */
    private final UserRepository userRepository;
    /**
     * The PlaceRepository responsible for database operations related to places.
     */
    private final PlaceRepository placeRepository;
    /**
     * The ReservationRepository responsible for database operations related to reservations.
     */
    private final ReservationRepository reservationRepository;

    /**
     * Retrieves a list of all reviews.
     *
     * @return A list of Review entities if reviews were found.
     * @throws ResourceNotFoundException if no reviews were found.
     */
    @Override
    public List<Review> getAllReviews() {
        List<Review> reviews = reviewRepository.findAll();
        checkIfReviewsWereFound(reviews);
        return reviews;
    }

    /**
     * Retrieves the user who made a specific review by review ID.
     *
     * @param reviewId The ID of the review for which to find the user.
     * @return A UserDTO object containing the user data if found, or null if not found.
     * @throws ResourceNotFoundException if the review is not found.
     */
    @Override
    public UserDTO getUserByReviewId(Integer reviewId) {
        Review review = getReviewById(reviewId);
        User user = review.getUser();
        UserDTO userDTO = MapToDTO.mapUserToDTO(user);

        return userDTO;
    }

    /**
     * Creates a new review and associates it with a place.
     *
     * @param placeId The ID of the place to associate with the review.
     * @param reviewRequest The review request containing review details.
     * @return The created Review entity.
     * @throws ResourceNotFoundException if the place or user is not found.
     * @throws CustomUserFriendlyException if the user hasn't made a reservation for the place or has already reviewed it.
     */
    @Override
    @Transactional
    public Review createReview(Integer placeId, ReviewRequest reviewRequest) {
        Place place = getPlaceById(placeId);
        User user = getUserById(reviewRequest.getUser().getId());
        checkIfCanPostReview(user, place);
        checkIfUserHasAlreadyReviewedThePlace(user, place);

        Review newReview = createNewReview(reviewRequest, user, place);
        return saveReview(newReview);
    }

    /**
     * Retrieves a list of reviews created by a specific user.
     *
     * @param userId The ID of the user.
     * @return A list of Review entities if reviews were found.
     * @throws ResourceNotFoundException if no reviews were found for the user.
     */
    @Override
    public List<Review> getReviewsByUserId(Integer userId) {
        User user = getUserById(userId);
        List<Review> reviews = reviewRepository.findByUser(user);
        checkIfReviewsWereFound(reviews);
        return reviews;
    }

    /**
     * Retrieves a review created by a specific user.
     *
     * @param userId The ID of the user.
     * @param reviewId The ID of the review to retrieve.
     * @return The Review entity if found.
     * @throws ResourceNotFoundException if the review is not found.
     */
    @Override
    public Review getReviewByUserId(Integer userId, Integer reviewId) {
        User user = getUserById(userId);
        Review review = getReviewById(reviewId);
        return review;
    }

    /**
     * Retrieves a list of reviews for a specific place.
     *
     * @param placeId The ID of the place.
     * @return A list of Review entities if reviews were found.
     * @throws ResourceNotFoundException if no reviews were found for the place.
     */
    @Override
    public List<Review> getReviewsByPlaceId(Integer placeId) {
        Place place = getPlaceById(placeId);
        List<Review> reviews = reviewRepository.findByPlace(place);
        checkIfReviewsWereFound(reviews);
        return reviews;
    }

    /**
     * Retrieves a review for a specific place.
     *
     * @param placeId The ID of the place.
     * @param reviewId The ID of the review to retrieve.
     * @return The Review entity if found.
     * @throws ResourceNotFoundException if the review is not found.
     */
    @Override
    public Review getReviewByPlaceId(Integer placeId, Integer reviewId) {
        Place place = getPlaceById(placeId);
        Review review = getReviewById(reviewId);
        return review;
    }

    /**
     * Updates an existing review.
     *
     * @param userId The ID of the user.
     * @param reviewId The ID of the review to update.
     * @param reviewRequest The updated review details.
     * @return The updated Review entity.
     * @throws ResourceNotFoundException if the review is not found.
     */
    @Override
    @Transactional
    public Review updateReview(Integer userId, Integer reviewId, ReviewRequest reviewRequest) {
        Review review = getReviewById(reviewId);

        copyOnlyNonNullProperties(reviewRequest, review);
        review.setUpdatedAt(LocalDateTime.now());
        reviewRepository.flush();
        return saveReview(review);
    }

    /**
     * Deletes a user's review.
     *
     * @param userId The ID of the user.
     * @param reviewId The ID of the review to delete.
     * @throws ResourceNotFoundException if the review is not found.
     */
    @Override
    @Transactional
    public void deleteReview(Integer userId, Integer reviewId) {
        User user = getUserById(userId);
        Review review = getReviewById(reviewId);
        reviewRepository.delete(review);
    }

    /**
     * Checks if a list of reviews is empty and throws a ResourceNotFoundException if no reviews were found.
     *
     * @param reviews The list of reviews to check.
     * @throws ResourceNotFoundException if no reviews were found.
     */
    private void checkIfReviewsWereFound(List<Review> reviews) {
        if (reviews.isEmpty()) {
            throw new ResourceNotFoundException("No reviews were found.");
        }
    }

    /**
     * Retrieves a Review entity by its unique ID.
     *
     * @param reviewId The ID of the review to retrieve.
     * @return The Review entity if found.
     * @throws ResourceNotFoundException if the review with the given ID is not found.
     */
    private Review getReviewById(Integer reviewId) {
        return reviewRepository.findById(reviewId)
                .orElseThrow(() -> new ResourceNotFoundException("Review not found."));
    }

    /**
     * Retrieves a Place entity by its unique ID.
     *
     * @param placeId The ID of the place to retrieve.
     * @return The Place entity if found.
     * @throws ResourceNotFoundException if the place with the given ID is not found.
     */
    private Place getPlaceById(Integer placeId) {
        return placeRepository.findById(placeId)
                .orElseThrow(() -> new ResourceNotFoundException("Place not found."));
    }

    /**
     * Retrieves a User entity by its unique ID.
     *
     * @param userId The ID of the user to retrieve.
     * @return The User entity if found.
     * @throws ResourceNotFoundException if the user with the given ID is not found.
     */
    private User getUserById(Integer userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found."));
    }

    /**
     * Checks if a user has made a reservation for a specific place.
     *
     * @param user The user for whom to check reservations.
     * @param place The place for which to check reservations.
     * @return {@code true} if the user has a reservation for the place, {@code false} otherwise.
     */
    private boolean userHasReservationForPlace(User user, Place place) {
        List<Reservation> reservations = reservationRepository.findByUserAndPlace(user, place);
        return !reservations.isEmpty();
    }

    /**
     * Checks if a user can post a review for a specific place.
     *
     * @param user The user who wants to post a review.
     * @param place The place for which the review is being posted.
     * @throws CustomUserFriendlyException if the user has not made a reservation for the place.
     */
    private void checkIfCanPostReview(User user, Place place) {
        if (!userHasReservationForPlace(user, place)) {
           throw new CustomUserFriendlyException("User has not made a reservation for this place.");
        }
    }

    /**
     * Checks if a user has already reviewed a specific place.
     *
     * @param user The user for whom to check reviews.
     * @param place The place for which to check reviews.
     * @return {@code true} if the user has already reviewed the place, {@code false} otherwise.
     */
    private boolean hasUserReviewedPlace(User user, Place place) {
        List<Review> existingReviews = reviewRepository.findByUserAndPlace(user, place);
        return !existingReviews.isEmpty();
    }

    /**
     * Checks if a user has already reviewed a specific place and throws an exception if they have.
     *
     * @param user The user who wants to post a review.
     * @param place The place for which the review is being posted.
     * @throws CustomUserFriendlyException if the user has already reviewed the place.
     */
    private void checkIfUserHasAlreadyReviewedThePlace(User user, Place place) {
        if (hasUserReviewedPlace(user, place)) {
            throw new CustomUserFriendlyException("You have already reviewed this place. You can only update your review.");
        }
    }

    /**
     * Creates a new review based on the provided ReviewRequest, user, and place.
     *
     * @param reviewRequest The request containing review details.
     * @param user The user who is creating the review.
     * @param place The place for which the review is being created.
     * @return The newly created Review entity.
     */
    private Review createNewReview(ReviewRequest reviewRequest, User user, Place place) {
        reviewRequest.validate();
        LocalDateTime now = LocalDateTime.now();

        Review review = Review.builder()
                .rating(reviewRequest.getRating())
                .comment(reviewRequest.getComment())
                .user(user)
                .place(place)
                .build();

        review.setCreatedAt(now);
        review.setUpdatedAt(now);

        return review;
    }

    /**
     * Copies non-null properties from a ReviewRequest to an existing Review entity.
     *
     * @param reviewRequest The request containing review details.
     * @param review The existing Review entity to update.
     */
    private void copyOnlyNonNullProperties(ReviewRequest reviewRequest, Review review) {
        LocalDateTime now = LocalDateTime.now();

        if (reviewRequest.getRating() != null) {
            review.setRating(reviewRequest.getRating());
        }
        if (reviewRequest.getComment() != null) {
            review.setComment(reviewRequest.getComment());
        }
    }

    /**
     * Saves a Review entity to the database.
     *
     * @param review The Review entity to be saved.
     * @return The saved Review entity.
     */
    private Review saveReview(Review review) {
        return reviewRepository.save(review);
    }

}
