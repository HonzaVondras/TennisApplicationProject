package com.inqoolApp.tennis;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.Rollback;

import com.inqoolApp.tennis.court.Court;

import com.inqoolApp.tennis.court.PriceList;
import com.inqoolApp.tennis.court.SurfaceType;
import com.inqoolApp.tennis.reservation.Reservation;
import com.inqoolApp.tennis.reservation.ReservationController;
import com.inqoolApp.tennis.user.User;
import com.inqoolApp.tennis.user.UserController;

import java.time.LocalDateTime;

import jakarta.transaction.Transactional;


@SpringBootTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class ReservationUserTest {



    private final GeneralRepository<Court> courtRepository = new GeneralRepositoryImpl<>(Court.class);

    private final GeneralRepository<Reservation> reservtionRepository = new GeneralRepositoryImpl<>(Reservation.class);

    private final GeneralRepository<User> userRepository = new GeneralRepositoryImpl<>(User.class);

    private static final PriceList priceList = new PriceList();

    private Long id = 1L;

	@Autowired
	private final ReservationController reservationController = new ReservationController(reservtionRepository, courtRepository, userRepository, priceList);

    @Autowired
    private final UserController userController = new UserController(userRepository);
    
    private final LocalDateTime time1 = LocalDateTime.of(2022, 4, 20, 10, 30, 0);
    private final LocalDateTime time2 = LocalDateTime.of(2022, 4, 20, 10, 45, 0);
    private final LocalDateTime time3 = LocalDateTime.of(2022, 4, 20, 11, 00, 0);
    private final LocalDateTime time4 = LocalDateTime.of(2022, 4, 20, 11, 30, 0);
    private final LocalDateTime time5 = LocalDateTime.of(2025, 4, 20, 11, 30, 0);
    private final LocalDateTime time6 = LocalDateTime.of(2025, 4, 20, 11, 30, 0);

    private final Reservation reservationA = new Reservation(null, 1L, "123456789", "Pavel Novák", time1, time2, false, false);
    private final Reservation reservationB = new Reservation(null, 1L, "223456789", "Karel Novák", time2, time3, true, false);
    private final Reservation reservationC = new Reservation(null, 1L, "323456789", "Marek Novák", time1, time3, false, false);
    private final Reservation reservationD = new Reservation(null, 2L, "323456789", "Marek Novák", time3, time4, false, false);
    private final Reservation reservationE = new Reservation(null, 1L, "323456789", "Marek Novák", time5, time6, false, false);


    @BeforeAll
	public static void setUp() {
		priceList.setPrice(SurfaceType.GRASS, 10.0);
        priceList.setPrice(SurfaceType.ASPHALT, 15.0);
        priceList.setPrice(SurfaceType.CLAY, 12.0);
        priceList.setPrice(SurfaceType.ARTIFICIAL_GRASS, 18.0);
	}

    @Test
	@Transactional
    @Rollback
	void AddReservationTest() {

		ResponseEntity<Double> price = reservationController.addReservation(reservationA);

        assertEquals(HttpStatus.OK, price.getStatusCode());
        assertEquals(150.0, price.getBody());

        ResponseEntity<List<Reservation>> reservationResponse = reservationController.getAllReservations();

		assertEquals(HttpStatus.OK, reservationResponse.getStatusCode());

		List<Reservation> expectedReservations = new ArrayList<>();
		expectedReservations.add(new Reservation(this.id, 1L, "123456789", "Pavel Novák", time1, time2, false, false));

		assertEquals(expectedReservations, reservationResponse.getBody());
    }

    @Test
    @Transactional
    @Rollback
    void AddReservationTestBad() {
        
        reservationController.addReservation(reservationA);
        ResponseEntity<Double> priceC = reservationController.addReservation(reservationC);

        assertEquals(HttpStatus.BAD_REQUEST, priceC.getStatusCode());

        ResponseEntity<List<Reservation>> reservationResponse = reservationController.getAllReservations();

		assertEquals(HttpStatus.OK, reservationResponse.getStatusCode());

		List<Reservation> expectedReservations = new ArrayList<>();
		expectedReservations.add(new Reservation(this.id, 1L, "123456789", "Pavel Novák", time1, time2, false, false));

		assertEquals(expectedReservations, reservationResponse.getBody());
    }


    @Test
    @Transactional
    @Rollback
    void AddReservationTestFour() {
	
		ResponseEntity<Double> priceB = reservationController.addReservation(reservationB);

        assertEquals(225.0, priceB.getBody());
        assertEquals(HttpStatus.OK, priceB.getStatusCode());

        ResponseEntity<List<Reservation>> reservationResponse = reservationController.getAllReservations();

		assertEquals(HttpStatus.OK, reservationResponse.getStatusCode());

		List<Reservation> expectedReservations = new ArrayList<>();
		expectedReservations.add(new Reservation(this.id, 1L, "223456789", "Karel Novák", time2, time3, true, false));


		assertEquals(expectedReservations, reservationResponse.getBody());
    }

    @Test
    @Transactional
    @Rollback
    void getReservationByIdTest() {

        reservationController.addReservation(reservationA);

        ResponseEntity<Reservation> resResponse = reservationController.getReservationById(id);

        assertEquals(HttpStatus.OK, resResponse.getStatusCode());

        assertEquals(new Reservation(this.id, 1L, "123456789", "Pavel Novák", time1, time2, false, false), resResponse.getBody());
    
    }

    @Test
    @Transactional
    @Rollback
    void updateReservationTestTest() {

        reservationController.addReservation(reservationA);
        ResponseEntity<Reservation> resResponse = reservationController.updateReservation(id, reservationB);

        assertEquals(HttpStatus.OK, resResponse.getStatusCode());

        ResponseEntity<Reservation> resResponse1 = reservationController.getReservationById(id);

        
        assertEquals(HttpStatus.OK, resResponse.getStatusCode());

        assertEquals(resResponse.getBody(), resResponse1.getBody());
    
    }



    @Test
    @Transactional
    @Rollback
    void userTest() {
        reservationController.addReservation(reservationA);
        reservationController.addReservation(reservationC);
        reservationController.addReservation(reservationB);
        this.id += 3;
        assertEquals(new User(1L,"Pavel Novák", "123456789", false), userController.getUserById(1L).getBody());
        assertEquals(new User(2L,"Marek Novák", "323456789", false), userController.getUserById(2L).getBody());
        assertEquals(new User(3L,"Karel Novák", "223456789", false), userController.getUserById(3L).getBody());
    
    }

    @Test
    @Transactional
    @Rollback
    void deleteReservationByIdTest() {
        reservationController.addReservation(reservationA);
        reservationController.addReservation(reservationB);

        ResponseEntity<HttpStatus> httpResponse = reservationController.deleteReservation(this.id);
        this.id++;

        assertEquals(HttpStatus.OK, httpResponse.getStatusCode());

        ResponseEntity<List<Reservation>> reservationResponse = reservationController.getAllReservations();

		assertEquals(HttpStatus.OK, reservationResponse.getStatusCode());

		List<Reservation> expectedReservations = new ArrayList<>();
		expectedReservations.add(new Reservation(this.id, 1L, "223456789", "Karel Novák", time2, time3, true, false));

		assertEquals(expectedReservations, reservationResponse.getBody());
    }

    @Test
    @Transactional
    @Rollback
    void deleteAllReservationsTest() {

        reservationController.addReservation(reservationA);
        reservationController.addReservation(reservationB);
        this.id+=2;

        ResponseEntity<HttpStatus> httpResponse = reservationController.deleteAllReservations();

        assertEquals(HttpStatus.OK, httpResponse.getStatusCode());

        ResponseEntity<List<Reservation>> reservationResponse = reservationController.getAllReservations();

		assertEquals(HttpStatus.NO_CONTENT, reservationResponse.getStatusCode());

		assertEquals(null, reservationResponse.getBody());
    }

    @Test
    @Transactional
    @Rollback
    void getReservationsByCourtIdTest() {

        reservationController.addReservation(reservationA);
        reservationController.addReservation(reservationB);
        reservationController.addReservation(reservationD);

        ResponseEntity<List<Reservation>> reservationResponse = reservationController.getReservationsByCourtId(1L);

		assertEquals(HttpStatus.OK, reservationResponse.getStatusCode());

		List<Reservation> expectedReservations = new ArrayList<>();
		expectedReservations.add(new Reservation(this.id, 1L, "123456789", "Pavel Novák", time1, time2, false, false));
        this.id++;
        expectedReservations.add(new Reservation(this.id, 1L, "223456789", "Karel Novák", time2, time3, true, false));
        this.id++;

		assertEquals(expectedReservations, reservationResponse.getBody());

        reservationResponse = reservationController.getReservationsByCourtId(2L);

		assertEquals(HttpStatus.OK, reservationResponse.getStatusCode());

		expectedReservations = new ArrayList<>();
		expectedReservations.add(new Reservation(this.id, 2L, "323456789", "Marek Novák", time3, time4, false, false));

		assertEquals(expectedReservations, reservationResponse.getBody());
	}


    @Test
    @Transactional
    @Rollback
    void getReservationsByPhoneNumberTest() {

        reservationController.addReservation(reservationC);
        reservationController.addReservation(reservationE);

        ResponseEntity<List<Reservation>> reservationResponse = reservationController.getReservationsByPhoneNumber("323456789");

		assertEquals(HttpStatus.OK, reservationResponse.getStatusCode());

		List<Reservation> expectedReservations = new ArrayList<>();
		expectedReservations.add(new Reservation(this.id, 1L, "323456789", "Marek Novák", time1, time3, false, false));
        this.id++;
        expectedReservations.add(new Reservation(this.id, 1L, "323456789", "Marek Novák", time5, time6, false, false));
        this.id++;

		assertEquals(expectedReservations, reservationResponse.getBody());

	}

    @Test
    @Transactional
    @Rollback
    void getPresentReservationsByPhoneNumberTest() {

        reservationController.addReservation(reservationC);
        reservationController.addReservation(reservationE);
        reservationController.addReservation(reservationD);

        ResponseEntity<List<Reservation>> reservationResponse = reservationController.getPresentReservationsByPhoneNumber("323456789");

		assertEquals(HttpStatus.OK, reservationResponse.getStatusCode());

		List<Reservation> expectedReservations = new ArrayList<>();
		expectedReservations.add(new Reservation(2L, 1L, "323456789", "Marek Novák", time5, time6, false, false));


		assertEquals(expectedReservations, reservationResponse.getBody());

	}



}
