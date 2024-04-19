package com.inqoolApp.tennis;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.inqoolApp.tennis.court.Court;
import com.inqoolApp.tennis.court.PriceList;
import com.inqoolApp.tennis.court.SurfaceType;
import com.inqoolApp.tennis.reservation.Reservation;
import com.inqoolApp.tennis.user.User;

/**
 * Class that has needed bean configuration and priceList setup
 *
 * @author Jan Vondrasek
*/

@Configuration
public class AppConfig {


    /**
    * Creates and initializes a PriceList bean.
    *
    * This method creates a PriceList bean and initializes it with predefined prices for different surface types.
    * The prices are set for each surface type: GRASS, ASPHALT, CLAY, and ARTIFICIAL_GRASS.
    *
    * @return the initialized PriceList bean with predefined prices for surface types
    */
    @Bean
    public PriceList priceList() {  
        PriceList priceList = new PriceList();
        priceList.setPrice(SurfaceType.GRASS, 10.0);
        priceList.setPrice(SurfaceType.ASPHALT, 15.0);
        priceList.setPrice(SurfaceType.CLAY, 12.0);
        priceList.setPrice(SurfaceType.ARTIFICIAL_GRASS, 18.0);
        return priceList;
    }

    /**
    * Creates a UserRepository bean.
    *
    * This method creates a UserRepository bean, which provides 
    * operations for managing User entities in the database.
    *
    * @return the created UserRepository bean
    */
    @Bean
    public GeneralRepository<User> userRepository() {
        return generalRepository(User.class);
    }

    /**
    * Creates a ReservationRepository bean.
    *
    * This method creates a ReservationRepository bean, which provides 
    * operations for managing Reservation entities in the database.
    *
    * @return the created ReservationRepository bean
    */
    @Bean
    public GeneralRepository<Reservation> reservationRepository() {
        return generalRepository(Reservation.class);
    }

    /**
    * Creates a CourtRepository bean.
    *
    * This method creates a CourtRepository bean, which provides 
    * operations for managing Court entities in the database.
    *
    * @return the created CourtRepository bean
    */
    @Bean
    public GeneralRepository<Court> courtRepository() {
        return generalRepository(Court.class);
    }
    
    /**
    * Creates a GeneralRepository bean for the specified entity class.
    *
    * This method creates a GeneralRepository bean for the specified entity class, 
    * providing generic operations for managing entities of that type in the database.
    *
    * @param entityClass the class representing the entity type for which the repository is created
    * @return the created GeneralRepository bean for the specified entity class
    *
    */
    private <T> GeneralRepository<T> generalRepository(Class<T> entityClass) {
        return new GeneralRepositoryImpl<>(entityClass);
    }

}
