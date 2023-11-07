package com.cameinw.cameinwbackend;

import com.cameinw.cameinwbackend.image.utils.ImageHandler;

import jakarta.annotation.PostConstruct;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * The main application class for Cameinw Backend.
 */
@SpringBootApplication
@EnableJpaAuditing
@EnableTransactionManagement
public class CameinwBackendApplication {

	/**
	 * The main method that starts the Spring Boot application.
	 *
	 * @param args Command-line arguments passed to the application.
	 */
	public static void main(String[] args) {
		SpringApplication.run(CameinwBackendApplication.class, args);
	}

	/**
	 * Initializes the application by setting up the image directory path.
	 * The image directory path is set based on the current working directory.
	 */
	@PostConstruct
	public void init() {

		String baseDirectory = System.getProperty("user.dir"); // current working directory
		String imageDirectory = baseDirectory + "/image_storage/"; // subdirectory for storing images
		ImageHandler.setMainPath(imageDirectory); // main path to the image directory

		// Alternatively, you can set the main path using a hardcoded value as follows:
//		ImageHandler.setMainPath("./cameinw_image_storage/");
	}

}
