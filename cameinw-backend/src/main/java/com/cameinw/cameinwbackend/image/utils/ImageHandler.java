package com.cameinw.cameinwbackend.image.utils;

import com.cameinw.cameinwbackend.user.model.User;
import com.cameinw.cameinwbackend.place.model.Place;
import com.cameinw.cameinwbackend.utilities.FileUtils;
import lombok.Getter;

import java.io.IOException;

/**
 * The ImageHandler class provides utility methods for handling image-related operations.
 */
public class ImageHandler {
    /**
     * The base directory path for user images.
     */
    private static final String USER_IMAGE_PATH = "users/";

    /**
     * The base directory path for place images.
     */
    private static final String PLACES_IMAGE_PATH = "places/";

    /**
     * The main directory path where images are stored.
     */
    @Getter
    private static String mainPath;

    /**
     * Sets the main directory path where images are stored.
     *
     * @param mainPath The main directory path.
     */
    public static void setMainPath(String mainPath) {
        ImageHandler.mainPath = mainPath;
    }

    /**
     * Creates a directory for storing place images.
     *
     * @param placeId The ID of the place for which the directory is created.
     * @return True if the directory is created successfully, false otherwise.
     */
    public static boolean createPlacesImageDirectory(Integer placeId) {
        String directoryPath = mainPath + PLACES_IMAGE_PATH + placeId;
        return FileUtils.makeDirectory(directoryPath);
    }

    /**
     * Creates a directory for storing user images.
     *
     * @param userId The ID of the user for which the directory is created.
     * @return True if the directory is created successfully, false otherwise.
     */
    public static boolean createUsersImageDirectory(Integer userId) {
        String directoryPath = mainPath + USER_IMAGE_PATH + userId;
        return FileUtils.makeDirectory(directoryPath);
    }


    /**
     * Saves an image with the specified name and content.
     *
     * @param imgName    The name of the image.
     * @param imgContent The content of the image.
     * @throws IOException If an I/O error occurs while saving the image.
     */
    public static void saveImage(String imgName, byte[] imgContent) throws IOException {
        String imgPath = mainPath + imgName;
        FileUtils.writeBytesToFile(imgPath, imgContent);
    }

    /**
     * Fetches the bytes of an image associated with a place.
     *
     * @param place   The place for which the image is fetched.
     * @param imgName The name of the image to fetch.
     * @return The bytes of the fetched image.
     * @throws IOException If an I/O error occurs while fetching the image.
     */
    public static byte[] fetchImageBytes(Place place, String imgName) throws IOException {
        String imgPath = ImageHandler.getMainPath() + PLACES_IMAGE_PATH + place.getId() + "/" + imgName;
        return FileUtils.readBytesFromFile(imgPath);
    }

    /**
     * Deletes an image at the specified path.
     *
     * @param imgPath The path of the image to delete.
     * @return True if the image is deleted successfully, false otherwise.
     */
    public static boolean deleteImage(String imgPath) {
        return FileUtils.deleteFile(imgPath);
    }

    /**
     * Fetches the bytes of a user's image.
     *
     * @param user The user for whom the image is fetched.
     * @return The bytes of the fetched user image.
     * @throws IOException If an I/O error occurs while fetching the user's image.
     */
    public static byte[] fetchUserImageBytes(User user) throws IOException {
        String imgName = user.getImageName();
        String imgPath = ImageHandler.getMainPath() + USER_IMAGE_PATH + user.getId() + "/" + imgName;

        return FileUtils.readBytesFromFile(imgPath);
    }

    /**
     * Fetches the bytes of a place's main image.
     *
     * @param place The place for which the main image is fetched.
     * @return The bytes of the fetched main image.
     * @throws IOException If an I/O error occurs while fetching the main image.
     */
    public static byte[] fetchPlacesMainImageBytes(Place place) throws IOException {
        String imgName = place.getMainImage();
        String imgPath = ImageHandler.getMainPath() + PLACES_IMAGE_PATH + place.getId() + "/" + imgName;

        return FileUtils.readBytesFromFile(imgPath);
    }

    /**
     * Deletes a user's image.
     *
     * @param userId    The ID of the user whose image is deleted.
     * @param imageName The name of the image to delete.
     * @return True if the user's image is deleted successfully, false otherwise.
     */
    public static boolean deleteUserImage(Integer userId, String imageName) {
        String imgPath = mainPath + USER_IMAGE_PATH + userId + "/" + imageName;
        return FileUtils.deleteFile(imgPath);
    }
}
