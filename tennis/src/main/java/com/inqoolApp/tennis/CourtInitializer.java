package com.inqoolApp.tennis;

import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import com.inqoolApp.tennis.court.Court;
import com.inqoolApp.tennis.court.SurfaceType;

import jakarta.transaction.Transactional;

/**
 * Class that initializes 4 courts
 *
 * @author Jan Vondrasek
*/

@Component
public class CourtInitializer {

    private final GeneralRepository<Court> courtRepository;


    /**
    * Constructor for CourtInitializer.
    *
    * Initializes the CourtInitializer with the provided CourtRepository instance.
    *
    * @param courtRepository offers operations for initializing and managing Court entities in the database
    */
    public CourtInitializer(GeneralRepository<Court> courtRepository) {
        this.courtRepository = courtRepository;
    }


    /**
    * Initializes courts in the database.
    *
    * This method initializes courts in the database by creating and saving Court entities.
    * The method creates Court entities for predefined courts with names, surface types, and deleted status.
    * The courts are saved using the CourtRepository provided by dependency injection.
    */
    @Bean
    @Transactional
    public void initializeCourts() {
        Court courtA = new Court(null, "Court A", SurfaceType.GRASS, false);
        Court courtB = new Court(null, "Court B", SurfaceType.GRASS, false);
        Court courtC = new Court(null, "Court C", SurfaceType.CLAY, false);
        Court courtD = new Court(null, "Court D", SurfaceType.CLAY, false);

        courtRepository.save(courtA);
        courtRepository.save(courtB);
        courtRepository.save(courtC);
        courtRepository.save(courtD);
    }
}
