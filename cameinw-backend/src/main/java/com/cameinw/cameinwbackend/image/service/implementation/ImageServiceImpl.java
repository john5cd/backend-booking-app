package com.cameinw.cameinwbackend.image.service.implementation;

import com.cameinw.cameinwbackend.exception.CustomUserFriendlyException;
import com.cameinw.cameinwbackend.exception.ResourceNotFoundException;
import com.cameinw.cameinwbackend.image.model.Image;
import com.cameinw.cameinwbackend.image.repository.ImageRepository;
import com.cameinw.cameinwbackend.image.service.ImageService;
import com.cameinw.cameinwbackend.place.model.Place;
import com.cameinw.cameinwbackend.place.model.Regulation;
import com.cameinw.cameinwbackend.place.repository.PlaceRepository;
import com.cameinw.cameinwbackend.user.model.User;
import com.cameinw.cameinwbackend.user.repository.UserRepository;
import com.cameinw.cameinwbackend.image.utils.ImageHandler;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

/**
 * The ImageServiceImpl class implements the ImageService interface and provides methods for managing images
 * and handling image-related operations.
 *
 * This service class is annotated with @Service to indicate that it is a Spring service component,
 * allowing it to be automatically discovered by Spring's component scanning and dependency injection mechanism.
 *
 * The @RequiredArgsConstructor annotation is used here to automatically generate a constructor for this class
 * that initializes the final fields marked with the 'final' keyword. This constructor simplifies the injection
 * of dependencies required by this service, such as ImageRepository, UserRepository, and PlaceRepository.
 *
 */
@RequiredArgsConstructor
@Service
public class ImageServiceImpl implements ImageService {
    /**
     * The ImageRepository responsible for database operations related to images.
     */
    private final ImageRepository imageRepository;
    /**
     * The UserRepository responsible for database operations related to users.
     */
    private final UserRepository userRepository;
    /**
     * The PlaceRepository responsible for database operations related to places.
     */
    private final PlaceRepository placeRepository;


    /**
     * The base path for storing images.
     */
    private static final String IMAGES_PATH = "/image_storage/";

    /**
     * The path for storing user images.
     */
    private static final String USER_IMAGE_PATH = "users/";

    /**
     * The path for storing place images.
     */
    private static final String PLACES_IMAGE_PATH = "places/";

    /**
     * Retrieves a list of all images.
     *
     * @return A list of Image objects.
     * @throws ResourceNotFoundException if no images were found.
     */
    @Override
    public List<Image> getAllImages() {

        List<Image> images = imageRepository.findAll();

        if (images.isEmpty()) {
            throw new ResourceNotFoundException("No images were found.");
        }
        return images;
    }


    /**
     * Retrieves the image associated with a specific user by user ID.
     *
     * @param userId The ID of the user.
     * @return A ResponseEntity containing the image data.
     * @throws ResourceNotFoundException       if the user or image was not found.
     * @throws CustomUserFriendlyException     if there was an error while fetching the image.
     */
    @Override
    public ResponseEntity<byte[]> getUserImage(Integer userId) {
        User user = getUserById(userId);
        try {
            byte[] imgData = ImageHandler.fetchUserImageBytes(user);
            return ResponseEntity.ok()
                    .contentType(MediaType.IMAGE_JPEG)
                    .body(imgData);
        } catch (IOException e) {
        throw new CustomUserFriendlyException("Error while fetching the image.");
        }
    }

    /**
     * Retrieves a list of images associated with a specific place by place ID.
     *
     * @param placeId The ID of the place.
     * @return An Optional containing a list of Image objects.
     */
    @Override
    public Optional<List<Image>> getImagesByPlaceId(Integer placeId) {
        return imageRepository.findByPlaceId(placeId);
    }

    /**
     * Uploads multiple images for a specific place.
     *
     * @param placeId     The ID of the place.
     * @param imageFiles  An array of MultipartFile objects representing the images to upload.
     * @throws ResourceNotFoundException   if the place was not found.
     * @throws CustomUserFriendlyException if there was an error while saving the images.
     */
    @Override
    @Transactional
    public void uploadImagesForPlace(Integer placeId, MultipartFile[] imageFiles) {
        for (MultipartFile imageFile : imageFiles) {
            saveSingleImage(placeId, imageFile);
        }
    }

    /**
     * Saves a single image associated with a place. This method creates the necessary directories, saves the image file,
     * and persists an Image entity in the database.
     *
     * @param placeId The ID of the place to which the image is associated.
     * @param imgFile The image file to be saved.
     * @throws CustomUserFriendlyException if there is an error while saving the image.
     */
    private void saveSingleImage(Integer placeId, MultipartFile imgFile) {
        Place place = getPlaceById(placeId);
        String imgName = imgFile.getOriginalFilename();

        try {
            ImageHandler.createPlacesImageDirectory(placeId);
            ImageHandler.saveImage(PLACES_IMAGE_PATH + placeId + "/" + imgName, imgFile.getBytes());
        } catch (IOException e) {
            throw new CustomUserFriendlyException("Error while saving the image.");
        }

        Image image = new Image();
        image.setImageName(imgName);
        image.setPlace(place);
        imageRepository.save(image);
    }

    /**
     * Uploads the main image for a specific place.
     *
     * @param placeId  The ID of the place.
     * @param imgFile  The MultipartFile object representing the main image to upload.
     * @throws ResourceNotFoundException   if the place was not found.
     * @throws CustomUserFriendlyException if there was an error while saving the main image.
     */
    @Override
    @Transactional
    public void uploadMainImageForPlace(Integer placeId,  MultipartFile imgFile) {
        Place place = getPlaceById(placeId);
        String imgName = imgFile.getOriginalFilename();

        try {
            ImageHandler.createPlacesImageDirectory(placeId);
            ImageHandler.saveImage(PLACES_IMAGE_PATH + placeId + "/" + imgName, imgFile.getBytes());
        } catch (IOException e) {
            throw new CustomUserFriendlyException("Error while saving the image.");
        }

        Image image = new Image();
        image.setImageName(imgName);
        image.setPlace(place);
        imageRepository.save(image);

        place.setMainImage(imgName);
        placeRepository.save(place);

    }

    /**
     * Updates the main image for a specific place.
     *
     * @param placeId  The ID of the place.
     * @param imgFile  The MultipartFile object representing the new main image.
     * @throws ResourceNotFoundException   if the place was not found.
     * @throws CustomUserFriendlyException if there was an error while saving the new main image.
     */
    @Override
    @Transactional
    public void updateMainImageForPlace(Integer placeId,  MultipartFile imgFile) {
        Place place = getPlaceById(placeId);
        String imgName = imgFile.getOriginalFilename();

        try {
            ImageHandler.createPlacesImageDirectory(placeId);
            ImageHandler.saveImage(PLACES_IMAGE_PATH + placeId + "/" + imgName, imgFile.getBytes());
        } catch (IOException e) {
            throw new CustomUserFriendlyException("Error while saving the image.");
        }

        place.setMainImage(imgName);
        placeRepository.save(place);

    }

    /**
     * Retrieves the main image of a specific place by place ID.
     *
     * @param placeId The ID of the place.
     * @return A ResponseEntity containing the main image data.
     * @throws ResourceNotFoundException       if the place or main image was not found.
     * @throws CustomUserFriendlyException     if there was an error while fetching the main image.
     */
    @Override
    public ResponseEntity<byte[]> getPlacesMainImage(Integer placeId) throws IOException {
        Place place = getPlaceById(placeId);
        try {
            byte[] imgData = ImageHandler.fetchPlacesMainImageBytes(place);
            return ResponseEntity.ok()
                    .contentType(MediaType.IMAGE_JPEG)
                    .body(imgData);
        } catch (IOException e) {
            throw new CustomUserFriendlyException("Error while fetching the image.");
        }
    }

    /**
     * Uploads a user's image by user ID.
     *
     * @param userId   The ID of the user.
     * @param imgFile  The MultipartFile object representing the user's image to upload.
     * @throws ResourceNotFoundException   if the user was not found.
     * @throws CustomUserFriendlyException if there was an error while saving the image.
     */
    @Override
    @Transactional
    public void uploadUserImage(Integer userId, MultipartFile imgFile) {
        User user = getUserById(userId);
        String imgName = imgFile.getOriginalFilename();

        try {
            ImageHandler.createUsersImageDirectory(userId);
            ImageHandler.saveImage(USER_IMAGE_PATH + userId + "/" + imgName, imgFile.getBytes());
        } catch (IOException ex) {
            throw new CustomUserFriendlyException("Error while saving the image.");
        }
        user.setImageName(imgName);
        userRepository.save(user);
    }

    /**
     * Updates a user's image by user ID.
     *
     * @param userId   The ID of the user.
     * @param imgFile  The MultipartFile object representing the new user's image.
     * @throws ResourceNotFoundException   if the user was not found.
     * @throws CustomUserFriendlyException if there was an error while saving the new user's image.
     */
    @Override
    @Transactional
    public void updateUserImage(Integer userId, MultipartFile imgFile) {
        User user = getUserById(userId);

        // Delete the previous image if it exists
        if (user.getImageName() != null) {
            ImageHandler.deleteUserImage(userId, user.getImageName());
        }

        String imgName = imgFile.getOriginalFilename();

        try {
            ImageHandler.createUsersImageDirectory(userId);
            ImageHandler.saveImage(USER_IMAGE_PATH + userId + "/" + imgName, imgFile.getBytes());
        } catch (IOException ex) {
            throw new CustomUserFriendlyException("Error while saving the image.");
        }


        user.setImageName(imgName);
        userRepository.save(user);
    }

    /**
     * Retrieves an image associated with a specific place and image ID.
     *
     * @param placeId The ID of the place.
     * @param imageId The ID of the image.
     * @return A ResponseEntity containing the image data.
     * @throws ResourceNotFoundException       if the place, image, or association was not found.
     * @throws CustomUserFriendlyException     if there was an error while fetching the image.
     */
    @Override
    public ResponseEntity<byte[]> getPlacesImage(Integer placeId, Integer imageId) {
        Place place = getPlaceById(placeId);
        Image img = getImageById(imageId);

        if (!img.getPlace().equals(place)) {
            throw new ResourceNotFoundException("Image is not associated with the Place.");
        }

        try {
            byte[] imgData = ImageHandler.fetchImageBytes(img.getPlace(), img.getImageName());
            return ResponseEntity.ok()
                    .contentType(MediaType.IMAGE_JPEG)
                    .body(imgData);
        } catch (IOException e) {
            throw new CustomUserFriendlyException("Error while fetching the image.");
        }

    }

    /**
     * Deletes an image associated with a specific place and image ID.
     *
     * @param placeId The ID of the place.
     * @param imageId The ID of the image.
     * @throws ResourceNotFoundException if the place, image, or association was not found.
     */
    @Override
    @Transactional
    public void deletePlacesImage(Integer placeId, Integer imageId) {
        Place place = getPlaceById(placeId);
        Image img = getImageById(imageId);

        if (!img.getPlace().equals(place)) {
            throw new ResourceNotFoundException("Image is not associated with the Place.");
        }

        String imgPath = IMAGES_PATH + img.getPlace().getId() + "/" + img.getImageName();
        ImageHandler.deleteImage(imgPath);
        imageRepository.delete(img);
    }

    /**
     * Retrieves an Image entity by its unique ID.
     *
     * @param imageId The ID of the image to retrieve.
     * @return The Image entity if found.
     * @throws ResourceNotFoundException if the image with the given ID is not found.
     */
    private Image getImageById(Integer imageId) {
        return imageRepository.findById(imageId)
                .orElseThrow(() -> new ResourceNotFoundException("Image not found"));
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
}
