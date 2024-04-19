package com.inqoolApp.tennis.reservation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.inqoolApp.tennis.GeneralRepository;
import com.inqoolApp.tennis.court.Court;
import com.inqoolApp.tennis.court.PriceList;
import com.inqoolApp.tennis.user.User;

import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.time.Duration;
import java.util.ArrayList;

/**
 * Class that handles all the work with the Reservations table in the database
 *
 * @author Jan Vondrasek
*/

@RestController
@RequestMapping("/api")
@AllArgsConstructor
@Getter
@Setter
public class ReservationController {

    private final GeneralRepository<Reservation> reservationRepository;

    private final GeneralRepository<Court> courtRepository;

    private final GeneralRepository<User> userRepository;

    private final PriceList priceList;


    /**
    * Constructs a new ReservationController instance.
    *
    * This constructor initializes a new ReservationController object with the provided repositories
    * and dependencies required for managing reservations, users, prices, and courts.
    *
    * @param reservationRepository the repository providing operations for managing Reservation entities
    * @param userRepository the repository providing operations for managing User entities
    * @param priceList the price list containing prices for different surface types
    * @param courtRepository the repository providing operations for managing Court entities
    */
    @Autowired
    public ReservationController(GeneralRepository<Reservation> reservationRepository, 
                                 GeneralRepository<User> userRepository, 
                                 PriceList priceList, 
                                 GeneralRepository<Court> courtRepository) {
        this.reservationRepository = reservationRepository;
        this.userRepository = userRepository;
        this.priceList = priceList;
        this.courtRepository = courtRepository;
    }

    /**
    * Retrieves all reservations.
    *
    * This method retrieves all reservations stored in the database.
    *
    * @return ResponseEntity containing a list of all reservations, or ResponseEntity
    *         with status NO_CONTENT if no reservations are found, or ResponseEntity
    *         with status INTERNAL_SERVER_ERROR if an exception occurs
    */
    @GetMapping("/getAllReservations")
    public ResponseEntity<List<Reservation>> getAllReservations() {
        try {
            List<Reservation> reservationList = new ArrayList<>();
            reservationRepository.findAll().forEach(reservationList::add);

            if (reservationList.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }

            return new ResponseEntity<>(reservationList, HttpStatus.OK);

        } catch(Exception ex){
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    /**
    * Retrieves a reservation by its unique identifier.
    *
    * This method retrieves a reservation from the database based on the provided ID.
    *
    * @param id the unique identifier of the reservation to retrieve
    * @return ResponseEntity containing the reservation with the specified ID, or ResponseEntity
    *         with status BAD_REQUEST if no reservation is found, or ResponseEntity
    *         with status INTERNAL_SERVER_ERROR if an exception occurs
    */
    @GetMapping("/getReservationById/{id}")
    public ResponseEntity<Reservation> getReservationById(@PathVariable Long id) {
        try {
            Reservation reservationObj = reservationRepository.findById(id);

            if (reservationObj == null){
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            }

            return new ResponseEntity<>(reservationObj, HttpStatus.OK);
    
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
        
    }

    /**
    * Adds a new reservation.
    *
    * This method adds a new reservation to the database, along with creating 
    * a new user if the user with the given phone number does not exist.
    * It also checks if there are any conflicting reservations for the 
    * specified court and time slot, calculates the price based on the reservation details,
    * and saves the reservation to the database.
    *
    * @param reservation the reservation object to add
    * @return ResponseEntity containing the calculated price of the reservation if added successfully, 
    *         or ResponseEntity with status BAD_REQUEST if there is a conflicting reservation, or ResponseEntity
    *         with status INTERNAL_SERVER_ERROR if an exception occurs
    */
    @Transactional
    @PostMapping("/addReservation")
    public ResponseEntity<Double> addReservation(@RequestBody Reservation reservation) {
        try {

            List<User> userObj = userRepository.getUserByPhoneNumber(reservation.getPhoneNumber(), User.class);
            if (userObj.isEmpty()){
                userRepository.save(new User(null, reservation.getFullName(), reservation.getPhoneNumber(), false));
            }

            List<Reservation> reservationCheck = reservationRepository
                                                .checkReservationTime(reservation.getCourtId(), 
                                                reservation.getStartTime(), reservation.getEndTime());
            if (!reservationCheck.isEmpty()){
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            }

            Duration duration = Duration.between(reservation.getStartTime(), reservation.getEndTime());
            long minutes = duration.toMinutes();
            Double finalPrice = minutes * priceList.getPrice(courtRepository
                                                   .findById(reservation.getCourtId()).getSurface());
            if (reservation.isFourPlayers()){
                finalPrice = finalPrice * 1.5;
            }

            reservationRepository.save(reservation);

            return new ResponseEntity<>(finalPrice, HttpStatus.OK);
            
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    /**
    * Updates an existing reservation.
    *
    * This method updates an existing reservation in the database with the provided ID,
    * by replacing its details with the ones provided in the request body.
    *
    * @param id the unique identifier of the reservation to update
    * @param reservation the updated reservation object
    * @return ResponseEntity containing the updated reservation object if updated successfully, or ResponseEntity
    *         with status BAD_REQUEST if the reservation with the specified ID is not found, or ResponseEntity
    *         with status INTERNAL_SERVER_ERROR if an exception occurs
    */
    @Transactional
    @PostMapping("/updateReservation/{id}")
    public ResponseEntity<Reservation> updateReservation(@PathVariable Long id, 
                                                         @RequestBody Reservation reservation) {
        try {

            Reservation reservationData = reservationRepository.findById(id);

            if (reservationData == null){
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            }

            Reservation updatedReservationtData = reservationData;
            updatedReservationtData.setCourtId(reservation.getCourtId());
            updatedReservationtData.setEndTime(reservation.getEndTime());
            updatedReservationtData.setFourPlayers(reservation.isFourPlayers());
            updatedReservationtData.setFullName(reservation.getFullName());
            updatedReservationtData.setPhoneNumber(reservation.getPhoneNumber());
            updatedReservationtData.setStartTime(reservation.getStartTime());
            updatedReservationtData.setDeleted(reservation.getDeleted());
            Reservation resObj = reservationRepository.save(updatedReservationtData);

            return new ResponseEntity<>(resObj, HttpStatus.OK);

        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
    * Deletes a reservation by its unique identifier.
    *
    * This method marks a reservation with the specified ID as deleted in the database.
    *
    * @param id the unique identifier of the reservation to delete
    * @return ResponseEntity with status OK if the reservation is successfully marked as deleted, or ResponseEntity
    *         with status BAD_REQUEST if the reservation with the specified ID is not found, or ResponseEntity
    *         with status INTERNAL_SERVER_ERROR if an exception occurs
    */
    @Transactional
    @DeleteMapping("/deleteReservationById/{id}")
    public ResponseEntity<HttpStatus> deleteReservation(@PathVariable Long id) {
        try {
            Reservation reservation =  reservationRepository.findById(id);

            if (reservation == null){
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            }

            reservation.setDeleted(true);
            ResponseEntity<Reservation> reservationResponse = this.updateReservation(id, reservation);

            if(reservationResponse.equals(new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR))){
                return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
            }

            return new ResponseEntity<>(HttpStatus.OK);

        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
        
    }

    /**
    * Deletes all reservations.
    *
    * This method deletes all reservations stored in the database by marking them as deleted.
    *
    * @return ResponseEntity with status OK if all reservations are successfully marked as deleted, or ResponseEntity
    *         with status NO_CONTENT if no reservations are found, or ResponseEntity
    *         with status INTERNAL_SERVER_ERROR if an exception occurs during the deletion process
    */
    @Transactional
    @DeleteMapping("/deleteAllReservations")
    public ResponseEntity<HttpStatus> deleteAllReservations() {
        try {
            List<Reservation> reservations = reservationRepository.findAll();
            if (reservations == null){
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }

            for(Reservation reservation : reservations){
                ResponseEntity<HttpStatus> reservationResponse = this.deleteReservation(reservation.getId());
                if(reservationResponse.equals(new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR))){
                    return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
                }
            }

            return new ResponseEntity<>(HttpStatus.OK);

        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
        
    }

    /**
    * Retrieves reservations by court ID.
    *
    * This method retrieves all reservations associated with the specified court ID from the database.
    *
    * @param id the unique identifier of the court
    * @return ResponseEntity containing a list of reservations associated with the specified court ID status OK
    */
    @Transactional
    @GetMapping("/getReservationsByCourtId/{id}")
    public ResponseEntity<List<Reservation>> getReservationsByCourtId(@PathVariable Long id) {
        List<Reservation> reservationList = new ArrayList<>();
        reservationRepository.getReservationsByCourtId(id).forEach(reservationList::add);

        return new ResponseEntity<>(reservationList, HttpStatus.OK);
    }

    /**
    * Retrieves reservations by phone number.
    *
    * This method retrieves all reservations associated with the specified phone number from the database.
    *
    * @param phoneNumber the phone number associated with the reservations
    * @return ResponseEntity containing a list of reservations associated with the specified phone number and status OK
    */
    @Transactional
    @GetMapping("/getReservationsByPhoneNumber/{phoneNumber}")
    public ResponseEntity<List<Reservation>> getReservationsByPhoneNumber(@PathVariable String phoneNumber) {
        List<Reservation> reservationList = new ArrayList<>();
        reservationRepository.getReservationsByPhoneNumber(phoneNumber).forEach(reservationList::add);

        return new ResponseEntity<>(reservationList, HttpStatus.OK);
    }

    /**
    * Retrieves present reservations by phone number.
    *
    * This method retrieves all present reservations associated with the specified phone number from the database.
    *
    * @param phoneNumber the phone number associated with the reservations to retrieve
    * @return ResponseEntity containing a list of present reservations 
    *         associated with the specified phone number and status OK 
    */
    @GetMapping("/getPresentReservationsByPhoneNumber/{phoneNumber}")
    public ResponseEntity<List<Reservation>> getPresentReservationsByPhoneNumber(@PathVariable String phoneNumber) {
        List<Reservation> reservationList = new ArrayList<>();
        reservationRepository.getPresentReservationsByPhoneNumber(phoneNumber).forEach(reservationList::add);

        return new ResponseEntity<>(reservationList, HttpStatus.OK);
    }
    
}
