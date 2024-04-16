package com.inqoolApp.tennis;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.inqoolApp.tennis.court.Court;
import com.inqoolApp.tennis.court.SurfaceType;

import jakarta.annotation.PostConstruct;
import jakarta.transaction.Transactional;

public class CourtInitializer {

    private final GeneralRepository<Court> courtRepository;

    @Autowired
    public CourtInitializer(GeneralRepository<Court> courtRepository) {
        this.courtRepository = courtRepository;
    }

    @Transactional
    @PostConstruct
    public void initializeCourts() {
        // Zde můžeš vytvořit a uložit kurtu do databáze přes courtRepository
        // Například:
        Court court1 = new Court("Court 1", SurfaceType.GRASS);
        Court court2 = new Court("Court 2", SurfaceType.CLAY);

        courtRepository.save(court1);
        courtRepository.save(court2);
    }
}
