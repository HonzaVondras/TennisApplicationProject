package com.inqoolApp.tennis;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Main of the SpringBoot application
 *
 * @author Jan Vondrasek
*/

@SpringBootApplication
public class TennisApplication {


    /**
    * Entry point for the Tennis application.
    *
    * This method is the entry point for the Tennis application. It starts the Spring Boot application
    * by running the SpringApplication with the provided configuration class and command-line arguments.
    *
    * @param args the command-line arguments passed to the application
    */
    public static void main(String[] args) {
        SpringApplication.run(TennisApplication.class, args);
	}
}
