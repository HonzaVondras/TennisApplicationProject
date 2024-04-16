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

import java.util.List;
import java.time.Duration;
import java.util.ArrayList;

@RestController
@RequestMapping("/api")
public class ReservationController {

    private final PriceList priceList;

    private final GeneralRepository<Reservation> reservationRepository;

    private final GeneralRepository<Court> courtRepository;

    private final GeneralRepository<User> userRepository;

    @Autowired
    public ReservationController(GeneralRepository<Reservation> reservationRepository, GeneralRepository<User> userRepository, PriceList priceList, GeneralRepository<Court> courtRepository) {
        this.reservationRepository = reservationRepository;
        this.userRepository = userRepository;
        this.priceList = priceList;
        this.courtRepository = courtRepository;
    }

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

    @Transactional
    @PostMapping("/addReservation")
    public ResponseEntity<Double> addReservation(@RequestBody Reservation reservation) {
        try {

            List<User> userObj = userRepository.getUserByPhoneNumber(reservation.getPhoneNumber(), User.class);
            if (userObj.isEmpty()){
                userRepository.save(new User(reservation.getFullName(), reservation.getPhoneNumber()));
            }

            List<Reservation> reservationCheck = reservationRepository.checkReservationTime(reservation.getCourtId(), reservation.getStartTime(), reservation.getEndTime());
            if (!reservationCheck.isEmpty()){
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            }

            Duration duration = Duration.between(reservation.getStartTime(), reservation.getEndTime());
            long minutes = duration.toMinutes();
            Double finalPrice = minutes * priceList.getPrice(courtRepository.findById(reservation.getCourtId()).getSurface());
            if (reservation.isFourPlayers()){
                finalPrice = finalPrice * 1.5;
            }

            reservationRepository.save(reservation);

            return new ResponseEntity<>(finalPrice, HttpStatus.OK);
            
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Transactional
    @PostMapping("/updateReservation/{id}")
    public ResponseEntity<Reservation> updateReservation(@PathVariable Long id, @RequestBody Reservation reservation) {
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

    @Transactional
    @GetMapping("/getReservationsByCourtId/{id}")
    public ResponseEntity<List<Reservation>> getReservationsByCourtId(@PathVariable Long id) {
        List<Reservation> reservationList = new ArrayList<>();
        reservationRepository.getReservationsByCourtId(id).forEach(reservationList::add);

        return new ResponseEntity<>(reservationList, HttpStatus.OK);
    }

    @Transactional
    @GetMapping("/getReservationsByPhoneNumber/{phoneNumber}")
    public ResponseEntity<List<Reservation>> getReservationsByPhoneNumber(@PathVariable String phoneNumber) {
        List<Reservation> reservationList = new ArrayList<>();
        reservationRepository.getReservationsByPhoneNumber(phoneNumber).forEach(reservationList::add);

        return new ResponseEntity<>(reservationList, HttpStatus.OK);
    }

    @GetMapping("/getPresentReservationsByPhoneNumber/{phoneNumber}")
    public ResponseEntity<List<Reservation>> getPresentReservationsByPhoneNumber(@PathVariable String phoneNumber) {
        List<Reservation> reservationList = new ArrayList<>();
        reservationRepository.getPresentReservationsByPhoneNumber(phoneNumber).forEach(reservationList::add);

        return new ResponseEntity<>(reservationList, HttpStatus.OK);
    }
    
}
