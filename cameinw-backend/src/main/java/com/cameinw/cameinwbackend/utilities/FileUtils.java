package com.cameinw.cameinwbackend.utilities;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * The FileUtils class provides utility methods for working with files and directories.
 * It includes methods for creating directories, deleting files, writing bytes to a file, and reading bytes from a file.
 */
public class FileUtils {
    /**
     * Creates a directory at the specified path if it does not already exist.
     *
     * @param directoryPath The path of the directory to create.
     * @return true if the directory was created or already exists, false otherwise.
     */
    public static boolean makeDirectory(String directoryPath) {
        File directory = new File(directoryPath);
        if (!directory.exists()) {
            return directory.mkdirs();
        }
        return false;
    }

    /**
     * Deletes a file at the specified path.
     *
     * @param filePath The path of the file to delete.
     * @return true if the file was successfully deleted, false otherwise.
     */
    public static boolean deleteFile(String filePath) {
        File file = new File(filePath);
        return file.delete();
    }

    /**
     * Writes the given byte array content to a file at the specified path.
     *
     * @param filePath The path of the file to write.
     * @param content  The byte array content to write to the file.
     * @throws IOException if an I/O error occurs while writing to the file.
     */
    public static void writeBytesToFile(String filePath, byte[] content) throws IOException {
        Path path = Paths.get(filePath);
        Files.write(path, content);
    }

    /**
     * Reads the contents of a file at the specified path as a byte array.
     *
     * @param filePath The path of the file to read.
     * @return The byte array containing the contents of the file.
     * @throws IOException if an I/O error occurs while reading from the file.
     */
    public static byte[] readBytesFromFile(String filePath) throws IOException {
        File file = new File(filePath);
        return org.apache.commons.io.FileUtils.readFileToByteArray(file);
    }
}
