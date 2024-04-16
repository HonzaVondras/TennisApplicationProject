package com.inqoolApp.tennis;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.inqoolApp.tennis.court.Court;
import com.inqoolApp.tennis.court.PriceList;
import com.inqoolApp.tennis.court.SurfaceType;
import com.inqoolApp.tennis.reservation.Reservation;
import com.inqoolApp.tennis.user.User;


@Configuration
public class AppConfig {

    @Bean
    public PriceList priceList() {  
        PriceList priceList = new PriceList();
        priceList.setPrice(SurfaceType.GRASS, 10.0);
        priceList.setPrice(SurfaceType.ASPHALT, 15.0);
        priceList.setPrice(SurfaceType.CLAY, 12.0);
        priceList.setPrice(SurfaceType.ARTIFICIAL_GRASS, 18.0);
        return priceList;
    }

    @Bean
    public GeneralRepository<User> userRepository() {
        return generalRepository(User.class);
    }

    @Bean
    public GeneralRepository<Reservation> reservationRepository() {
        return generalRepository(Reservation.class);
    }

    @Bean
    public GeneralRepository<Court> courtRepository() {
        return generalRepository(Court.class);
    }

    private <T> GeneralRepository<T> generalRepository(Class<T> entityClass) {
        return new GeneralRepositoryImpl<>(entityClass);
    }

}
