package com.cameinw.cameinwbackend.image.service;

import com.cameinw.cameinwbackend.image.model.Image;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

/**
 * The ImageService interface defines methods for managing images and handling image-related operations.
 * It is used for operations such as uploading, updating, and retrieving images.
 */
public interface ImageService {
    /**
     * Retrieve a list of all images.
     *
     * @return A list of Image objects.
     */
    List<Image> getAllImages();

    /**
     * Retrieve a user's image by userId.
     *
     * @param userId The unique identifier of the user.
     * @return A ResponseEntity containing the user's image as a byte array, or an error response if not found.
     * @throws IOException if an error occurs while fetching the image.
     */
    ResponseEntity<byte[]> getUserImage(Integer userId) throws IOException;

    /**
     * Upload a user's image.
     *
     * @param userId   The unique identifier of the user.
     * @param imgFile  The image file to upload.
     * @throws IOException if an error occurs while uploading the image.
     */
    void uploadUserImage(Integer userId, MultipartFile imgFile) throws IOException;

    /**
     * Update a user's image.
     *
     * @param userId   The unique identifier of the user.
     * @param imgFile  The updated image file.
     * @throws IOException if an error occurs while updating the image.
     */
    void updateUserImage(Integer userId, MultipartFile imgFile) throws IOException;

    /**
     * Retrieve a list of images associated with a specific place by placeId.
     *
     * @param placeId The unique identifier of the place.
     * @return An optional list of images associated with the place, or an empty optional if none are found.
     */
    Optional<List<Image>> getImagesByPlaceId(Integer placeId);

    /**
     * Upload multiple images for a place.
     *
     * @param placeId     The unique identifier of the place.
     * @param imageFiles  An array of image files to upload.
     * @throws IOException if an error occurs while uploading the images.
     */
    void uploadImagesForPlace(Integer placeId, MultipartFile[] imageFiles) throws IOException;

    /**
     * Upload the main image for a place.
     *
     * @param placeId The unique identifier of the place.
     * @param imgFile The main image file to upload.
     * @throws IOException if an error occurs while uploading the main image.
     */
    void uploadMainImageForPlace(Integer placeId, MultipartFile imgFile) throws IOException;

    /**
     * Update the main image for a place.
     *
     * @param placeId The unique identifier of the place.
     * @param imgFile The updated main image file.
     * @throws IOException if an error occurs while updating the main image.
     */
    public void updateMainImageForPlace(Integer placeId,  MultipartFile imgFile) throws IOException;

    /**
     * Retrieve an image associated with a specific place and imageId.
     *
     * @param placeId The unique identifier of the place.
     * @param imageId The unique identifier of the image.
     * @return A ResponseEntity containing the image as a byte array, or an error response if not found.
     * @throws IOException if an error occurs while fetching the image.
     */
    ResponseEntity<byte[]> getPlacesImage(Integer placeId, Integer imageId) throws IOException;

    /**
     * Retrieve the main image for a place by placeId.
     *
     * @param placeId The unique identifier of the place.
     * @return A ResponseEntity containing the main image as a byte array, or an error response if not found.
     * @throws IOException if an error occurs while fetching the main image.
     */
    ResponseEntity<byte[]> getPlacesMainImage(Integer placeId) throws IOException;

    /**
     * Delete an image associated with a specific place by placeId and imageId.
     *
     * @param placeId The unique identifier of the place.
     * @param imageId The unique identifier of the image.
     */
    void deletePlacesImage(Integer placeId, Integer imageId);
}
