package com.cameinw.cameinwbackend.image.controller;

import com.cameinw.cameinwbackend.exception.ResourceNotFoundException;
import com.cameinw.cameinwbackend.image.model.Image;
import com.cameinw.cameinwbackend.image.service.ImageService;
import com.cameinw.cameinwbackend.response.GenericResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

/**
 * The ImageController class defines RESTful endpoints for managing and retrieving images.
 *
 * This class is annotated with:
 * - @RestController: Marks this class as a REST controller, allowing it to handle HTTP requests.
 * - @RequestMapping("/api/images"): Specifies the base URL path for all endpoints in this controller.
 * - @RequiredArgsConstructor: automatically generates a constructor for this class that injects the final fields declared in the class.
 */
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/images")
public class ImageController {
    /**
     * The ImageService used for image-related operations.
     */
    private final ImageService imageService;


    /**
     * Retrieves a list of all images.
     *
     * @return A ResponseEntity containing the list of images if found, or an error response if not found.
     * @throws ResourceNotFoundException if no images are found.
     */
    @GetMapping() //CHECK OK
    public ResponseEntity<Object> getAllImages() {
        GenericResponse genericResponse = new GenericResponse();
        try {
            List<Image> images = imageService.getAllImages();
            return new ResponseEntity<>(images, HttpStatus.OK);
        } catch (ResourceNotFoundException ex) {
            genericResponse.setMessage(ex.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(genericResponse);
        }
    }


    /**
     * Retrieves an image by user ID.
     *
     * @param userId The ID of the user.
     * @return A ResponseEntity containing the image data if found, or an error response if not found or an error occurs.
     * @throws ResourceNotFoundException if the user image is not found.
     * @throws IOException if there is an error while retrieving the image.
     */
    @GetMapping("/users/{user_id}/image") //CHECK OK!!!!!
    public ResponseEntity<byte[]> getImageByUserId(@PathVariable("user_id") Integer userId) throws IOException {
        try {
            return imageService.getUserImage(userId);
        } catch (ResourceNotFoundException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        } catch (IOException ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    /**
     * Uploads an image for a user.
     *
     * @param userId   The ID of the user.
     * @param imgFile  The image file to upload.
     * @return A ResponseEntity with a success message or an error response if not found or an error occurs.
     * @throws ResourceNotFoundException if the user is not found.
     * @throws IOException if there is an error while uploading the image.
     */
    @PostMapping("/users/{user_id}/image") //CHECK OK!!!!!
    public ResponseEntity<Object> uploadUserImage(
            @PathVariable("user_id") Integer userId,
            @RequestParam("image") MultipartFile imgFile) throws IOException {
        GenericResponse genericResponse = new GenericResponse();
        try {
            imageService.uploadUserImage(userId, imgFile);
            genericResponse.setMessage("Image uploaded successfully.");
            return ResponseEntity.status(HttpStatus.CREATED).body(genericResponse);
        } catch (ResourceNotFoundException ex) {
            genericResponse.setMessage(ex.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(genericResponse);
        } catch (IOException ex) {
            genericResponse.setMessage(ex.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(genericResponse);
        }
    }

    /**
     * Updates a user's image.
     *
     * @param userId  The ID of the user.
     * @param imgFile The image file to update.
     * @return A ResponseEntity with a success message or an error response if not found or an error occurs.
     * @throws ResourceNotFoundException if the user or image to update is not found.
     * @throws IOException if there is an error while updating the image.
     */
    @PutMapping("/users/{user_id}/image") //-------check ok-----------
    public ResponseEntity<Object> updateUserImage(
            @PathVariable("user_id") Integer userId,
            @RequestParam("image") MultipartFile imgFile) throws IOException{
        GenericResponse genericResponse = new GenericResponse();
        try {
            imageService.updateUserImage(userId, imgFile);
            genericResponse.setMessage("Image updated successfully.");
            return ResponseEntity.status(HttpStatus.OK).body(genericResponse);
        } catch (ResourceNotFoundException ex) {
            genericResponse.setMessage(ex.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(genericResponse);
        } catch (IOException ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(genericResponse);
        }
    }

    /**
     * Retrieves a list of images by place ID.
     *
     * @param placeId The ID of the place.
     * @return A ResponseEntity containing the list of images if found, or a 404 response if not found.
     * @throws ResourceNotFoundException if no images are found for the place.
     */
    @GetMapping("/places/{place_id}/gallery") //CHECK OK!!!!!
    public ResponseEntity<List<Image>> getImagesByPlaceId(@PathVariable("place_id") Integer placeId) {
        Optional<List<Image>> images = imageService.getImagesByPlaceId(placeId);
        return images.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build()); // STATUS: 404 Not Found
    }

    /**
     * Uploads images for a place.
     *
     * @param placeId  The ID of the place.
     * @param imgFiles The image files to upload.
     * @return A ResponseEntity with a success message or an error response if not found or an error occurs.
     * @throws ResourceNotFoundException if the place is not found.
     * @throws IOException if there is an error while uploading the images.
     */
    @PostMapping("/places/{place_id}/gallery") //CHECK OK!!!!!
    public ResponseEntity<Object> uploadImagesForPlace(
            @PathVariable("place_id") Integer placeId,
            @RequestParam("images") MultipartFile[] imgFiles
    ) throws IOException {
        GenericResponse genericResponse = new GenericResponse();
        try {
            imageService.uploadImagesForPlace(placeId, imgFiles);
            genericResponse.setMessage("Images uploaded successfully.");
            return ResponseEntity.status(HttpStatus.OK).body(genericResponse);
        } catch (ResourceNotFoundException ex) {
            genericResponse.setMessage(ex.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(genericResponse);
        } catch (IOException ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(genericResponse);
        }
    }

    /**
     * Uploads the main image for a place.
     *
     * @param placeId The ID of the place.
     * @param imgFile The main image file to upload.
     * @return A ResponseEntity with a success message or an error response if not found or an error occurs.
     * @throws ResourceNotFoundException if the place is not found.
     * @throws IOException if there is an error while uploading the main image.
     */
    @PostMapping("/places/{place_id}/main-image") //CHECK OK!!!!!
    public ResponseEntity<Object> uploadMainImageForPlace(
            @PathVariable("place_id") Integer placeId,
            @RequestParam("image") MultipartFile imgFile
    ) throws IOException {
        GenericResponse genericResponse = new GenericResponse();
        try {
            imageService.uploadMainImageForPlace(placeId, imgFile);
            genericResponse.setMessage("Main image uploaded successfully.");
            return ResponseEntity.status(HttpStatus.OK).body(genericResponse);
        } catch (ResourceNotFoundException ex) {
            genericResponse.setMessage(ex.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(genericResponse);
        } catch (IOException ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(genericResponse);
        }
    }

    /**
     * Updates the main image for a place.
     *
     * @param placeId The ID of the place.
     * @param imgFile The main image file to update.
     * @return A ResponseEntity with a success message or an error response if not found or an error occurs.
     * @throws ResourceNotFoundException if the place or image to update is not found.
     * @throws IOException if there is an error while updating the main image.
     */
    @PutMapping("/places/{place_id}/main-image") //-------check ok-----------
    public ResponseEntity<Object> updateMainImageForPlace(
            @PathVariable("place_id") Integer placeId,
            @RequestParam("image") MultipartFile imgFile) throws IOException{
        GenericResponse genericResponse = new GenericResponse();
        try {
            imageService.updateMainImageForPlace(placeId, imgFile);
            genericResponse.setMessage("Main image updated successfully.");
            return ResponseEntity.status(HttpStatus.OK).body(genericResponse);
        } catch (ResourceNotFoundException ex) {
            genericResponse.setMessage(ex.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(genericResponse);
        } catch (IOException ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(genericResponse);
        }
    }

    /**
     * Retrieves the main image of a place by place ID.
     *
     * @param placeId The ID of the place.
     * @return A ResponseEntity containing the main image data if found, or an error response if not found or an error occurs.
     * @throws ResourceNotFoundException if the main image for the place is not found.
     * @throws IOException if there is an error while retrieving the main image.
     */
    @GetMapping("/places/{place_id}/main-image") //CHECK OK!!!!!
    public ResponseEntity<byte[]> getPlacesMainImage(@PathVariable("place_id") Integer placeId) throws IOException{
        try {
            return imageService.getPlacesMainImage(placeId);
        } catch (ResourceNotFoundException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        } catch (IOException ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    /**
     * Retrieves an image of a place by place and image ID.
     *
     * @param placeId The ID of the place.
     * @param imageId The ID of the image.
     * @return A ResponseEntity containing the image data if found, or an error response if not found or an error occurs.
     * @throws ResourceNotFoundException if the place or image is not found.
     * @throws  IOException if there is an error while retrieving the image.
     */
    @GetMapping("/places/{place_id}/gallery/{image_id}") //CHECK OK!!!!!
    public ResponseEntity<byte[]> getPlaceImageById(
            @PathVariable("place_id") Integer placeId,
            @PathVariable("image_id") Integer imageId
    ) throws IOException {
        try {
            return imageService.getPlacesImage(placeId, imageId);
        } catch (ResourceNotFoundException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        } catch (IOException ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    /**
     * Deletes an image of a place by place and image ID.
     *
     * @param placeId The ID of the place.
     * @param imageId The ID of the image to delete.
     * @return A ResponseEntity with a success message or an error response if not found or an error occurs.
     * @throws ResourceNotFoundException if the place or image to delete is not found.
     */
    @DeleteMapping("/places/{place_id}/gallery/{image_id}") //CHECK OK!!!!!
    public ResponseEntity<Object> deletePlaceImageById(@PathVariable("place_id") Integer placeId, @PathVariable("image_id") Integer imageId) {
        GenericResponse genericResponse = new GenericResponse();
        try {
            imageService.deletePlacesImage(placeId, imageId);
            genericResponse.setMessage("Place's image deleted successfully.");
            return ResponseEntity.status(HttpStatus.NO_CONTENT).body(genericResponse);
        } catch (ResourceNotFoundException ex) {
            genericResponse.setMessage(ex.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(genericResponse);
        }
    }
}
