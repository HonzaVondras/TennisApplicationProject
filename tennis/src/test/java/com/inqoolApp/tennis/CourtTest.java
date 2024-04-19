package com.inqoolApp.tennis;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.annotation.Rollback;

import com.inqoolApp.tennis.court.Court;

import com.inqoolApp.tennis.court.CourtController;
import com.inqoolApp.tennis.court.SurfaceType;

import jakarta.transaction.Transactional;

@SpringBootTest
class CourtTest {

	private final GeneralRepository<Court> courtRepository = new GeneralRepositoryImpl<>(Court.class);

	@Autowired
	private final CourtController courtController = new CourtController(courtRepository);

	private final Court courtA = new Court(1L, "Court A", SurfaceType.GRASS, false);
	private final Court courtB = new Court(2L, "Court B", SurfaceType.GRASS, false);
	private final Court courtC = new Court(3L, "Court C", SurfaceType.CLAY, false);
	private final Court courtD = new Court(4L, "Court D", SurfaceType.CLAY, false);
	private final Court courtE = new Court(null, "Court E", SurfaceType.ARTIFICIAL_GRASS, false);
	private final Court courtF = new Court(null, "Court F", SurfaceType.ASPHALT, false);


	@Test
	@Transactional
	@Rollback
	void getAllCourtsTest() {
		ResponseEntity<List<Court>> courtsResponse = courtController.getAllCourts();

		assertEquals(HttpStatus.OK, courtsResponse.getStatusCode());

		List<Court> expectedCourts = new ArrayList<>();
		expectedCourts.add(courtA);
		expectedCourts.add(courtB);
		expectedCourts.add(courtC);
		expectedCourts.add(courtD);

		assertEquals(expectedCourts, courtsResponse.getBody());
	}

	@Test
	@Transactional
	@Rollback
	void addCourtTest(){
		ResponseEntity<Court> courtCheck = courtController.addCourt(courtE);
		courtController.addCourt(courtF);

		assertEquals(courtCheck.getBody(), courtE);

		ResponseEntity<List<Court>> courtsResponse = courtController.getAllCourts();

		assertEquals(HttpStatus.OK, courtsResponse.getStatusCode());


		List<Court> expectedCourts = new ArrayList<>();
		expectedCourts.add(courtA);
		expectedCourts.add(courtB);
		expectedCourts.add(courtC);
		expectedCourts.add(courtD);
		expectedCourts.add(courtE);
		expectedCourts.add(courtF);

		assertEquals(expectedCourts, courtsResponse.getBody());
	}

	@Test
	@Transactional
	@Rollback
	void getCourtByIdTest() {
		ResponseEntity<Court> courtsResponse = courtController.getCourtById(2L);
		assertEquals(HttpStatus.OK, courtsResponse.getStatusCode());
		assertEquals(courtB, courtsResponse.getBody());
	}

	@Test
	@Transactional
	@Rollback
	void getCourtByIdTestBad() {
		ResponseEntity<Court> courtsResponse = courtController.getCourtById(942L);
		assertEquals(HttpStatus.BAD_REQUEST, courtsResponse.getStatusCode());
	}

	@Test
	@Transactional
	@Rollback
	void updateCourtTest(){
		ResponseEntity<Court> courtCheck = courtController.updateCourt(1L, courtB);

		assertEquals(courtCheck.getBody(), new Court(1L, "Court B", SurfaceType.GRASS, false));
		assertEquals(courtCheck.getStatusCode(), HttpStatus.OK);

		ResponseEntity<List<Court>> courtsResponse = courtController.getAllCourts();

		assertEquals(HttpStatus.OK, courtsResponse.getStatusCode());

		List<Court> expectedCourts = new ArrayList<>();
		expectedCourts.add(new Court(1L, "Court B", SurfaceType.GRASS, false));
		expectedCourts.add(courtB);
		expectedCourts.add(courtC);
		expectedCourts.add(courtD);

		assertEquals(expectedCourts, courtsResponse.getBody());
	}

	@Test
	@Transactional
	@Rollback
	void updateCourtTestErr(){
		ResponseEntity<Court> courtCheck = courtController.updateCourt(5L, courtB);

		assertEquals(courtCheck.getStatusCode(), HttpStatus.BAD_REQUEST);

		ResponseEntity<List<Court>> courtsResponse = courtController.getAllCourts();

		assertEquals(HttpStatus.OK, courtsResponse.getStatusCode());

		List<Court> expectedCourts = new ArrayList<>();
		expectedCourts.add(courtA);
		expectedCourts.add(courtB);
		expectedCourts.add(courtC);
		expectedCourts.add(courtD);

		assertEquals(expectedCourts, courtsResponse.getBody());
	}

	@Test
	@Transactional
	@Rollback
	void deleteCourtByIdTest(){
		ResponseEntity<HttpStatus> courtCheck = courtController.deleteCourt(1L);

		assertEquals(courtCheck.getStatusCode(), HttpStatus.OK);

		ResponseEntity<List<Court>> courtsResponse = courtController.getAllCourts();

		assertEquals(HttpStatus.OK, courtsResponse.getStatusCode());

		List<Court> courts = courtsResponse.getBody();

		List<Court> expectedCourts = new ArrayList<>();
		expectedCourts.add(courtB);
		expectedCourts.add(courtC);
		expectedCourts.add(courtD);

		assertEquals(expectedCourts, courts);

		ResponseEntity<Court> deletedResponse = courtController.getCourtById(1L);

		assertEquals(HttpStatus.OK, deletedResponse.getStatusCode());
		assertEquals(new Court(1L, "Court A", SurfaceType.GRASS, true), deletedResponse.getBody());
	}

	@Test
	@Transactional
	@Rollback
	void deleteCourtByIdTestErr(){
		ResponseEntity<HttpStatus> courtCheck = courtController.deleteCourt(20L);

		assertEquals(courtCheck.getStatusCode(), HttpStatus.BAD_REQUEST);

		ResponseEntity<List<Court>> courtsResponse = courtController.getAllCourts();

		assertEquals(HttpStatus.OK, courtsResponse.getStatusCode());

		List<Court> expectedCourts = new ArrayList<>();
		expectedCourts.add(courtA);
		expectedCourts.add(courtB);
		expectedCourts.add(courtC);
		expectedCourts.add(courtD);

		assertEquals(expectedCourts, courtsResponse.getBody());
	}

	@Test
	@Transactional
	@Rollback
	void deleteAllCourtsTest(){
		ResponseEntity<HttpStatus> courtCheck = courtController.deleteAllCourts();

		assertEquals(courtCheck.getStatusCode(), HttpStatus.OK);

		ResponseEntity<List<Court>> courtsResponse = courtController.getAllCourts();

		assertEquals(HttpStatus.NO_CONTENT, courtsResponse.getStatusCode());

		assertEquals(null, courtsResponse.getBody());

		ResponseEntity<Court> courtResponse = courtController.getCourtById(1L);
		assertEquals(HttpStatus.OK, courtResponse.getStatusCode());
		assertEquals(new Court(1L, "Court A",  SurfaceType.GRASS, true), courtResponse.getBody());

		courtResponse = courtController.getCourtById(2L);
		assertEquals(HttpStatus.OK, courtResponse.getStatusCode());
		assertEquals(new Court(2L, "Court B",  SurfaceType.GRASS, true), courtResponse.getBody());

		courtResponse = courtController.getCourtById(3L);
		assertEquals(HttpStatus.OK, courtResponse.getStatusCode());
		assertEquals(new Court(3L, "Court C",  SurfaceType.CLAY, true), courtResponse.getBody());

		courtResponse = courtController.getCourtById(4L);
		assertEquals(HttpStatus.OK, courtResponse.getStatusCode());
		assertEquals(new Court(4L, "Court D",  SurfaceType.CLAY, true), courtResponse.getBody());
		
	}
 
}
