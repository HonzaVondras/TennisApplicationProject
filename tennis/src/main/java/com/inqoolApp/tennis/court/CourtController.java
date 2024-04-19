package com.inqoolApp.tennis.court;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.inqoolApp.tennis.GeneralRepository;

import jakarta.transaction.Transactional;

import lombok.*;

import java.util.List;
import java.util.ArrayList;


/**
 * Class that handles all work with the Courts table in the database
 *
 * @author Jan Vondrasek
*/

@RestController
@RequestMapping("/api")
@Getter
@Setter
public class CourtController {
    
    @Autowired
    private final GeneralRepository<Court> courtRepository;


    /**
    * Constructs a new CourtController instance.
    *
    * This constructor initializes a new CourtController object with the provided
    * Court repository, which offers operations on EntityManager or raw SQL commands
    * on the database.
    *
    * @param courtRepository the repository providing operations for managing Court entities
    */
    @Autowired
    public CourtController(GeneralRepository<Court> courtRepository) {
        this.courtRepository = courtRepository;
    }


    /**
    * Retrieves all courts.
    *
    * This method retrieves all courts available in the system.
    *
    * @return ResponseEntity containing a list of all courts, or ResponseEntity with status
    *         NO_CONTENT if no courts are found, or INTERNAL_SERVER_ERROR if an exception occurs
    */
    @GetMapping("/getAllCourts")
    public ResponseEntity<List<Court>> getAllCourts() {
        try {
            List<Court> courtList = new ArrayList<>();
            courtRepository.findAll().forEach(courtList::add);

            if (courtList.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }

            return new ResponseEntity<>(courtList, HttpStatus.OK);

        } catch(Exception ex) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
    * Retrieves a court by its unique identifier.
    *
    * This method retrieves a court from the database based on the provided ID.
    *
    * @param id the unique identifier of the court to retrieve
    * @return ResponseEntity containing the court with the specified ID, or ResponseEntity
    *         with status BAD_REQUEST if no court is found, or INTERNAL_SERVER_ERROR if an exception occurs
    */
    @GetMapping("/getCourtById/{id}")
    public ResponseEntity<Court> getCourtById(@PathVariable Long id) {
        try {

            Court courtObj = courtRepository.findById(id);

            if (courtObj == null){
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            }

            return new ResponseEntity<>(courtObj, HttpStatus.OK);

        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    /**
    * Adds a new court to the database.
    *
    * This method saves the provided court object to the database.
    *
    * @param court the court object to add to the database
    * @return ResponseEntity containing the added court object with its database-assigned identifier,
    *         or ResponseEntity with status INTERNAL_SERVER_ERROR if an exception occurs
    */
    @Transactional
    @PostMapping("/addCourt")
    public ResponseEntity<Court> addCourt(@RequestBody Court court) {
    
        try {

            Court courtObj = courtRepository.save(court);
            return new ResponseEntity<>(courtObj, HttpStatus.OK);

        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
        
    }


    /**
    * Updates an existing court in the database.
    *
    * This method updates the details of the court with the specified ID in the database
    * using the provided court object.
    *
    * @param id the unique identifier of the court to update
    * @param court the court object containing the updated details
    * @return ResponseEntity containing the updated court object with its database-assigned identifier,
    *         or ResponseEntity with status BAD_REQUEST if the court with the specified ID is not found,
    *         or ResponseEntity with status INTERNAL_SERVER_ERROR if an exception occurs
    */
    @Transactional
    @PutMapping("/updateCourt/{id}")
    public ResponseEntity<Court> updateCourt(@PathVariable Long id, @RequestBody Court court) {
        try {

            Court courtData = courtRepository.findById(id);

            if(courtData == null){
               return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            }
    
            Court updatedCourtData = courtData;
            updatedCourtData.setName(court.getName());
            updatedCourtData.setSurface(court.getSurface());
            updatedCourtData.setDeleted(court.getDeleted());
            Court courtObj = courtRepository.save(updatedCourtData);

            return new ResponseEntity<>(courtObj, HttpStatus.OK);
            
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
        
    }

    /**
    * Marks a court as deleted in the database.
    *
    * This method marks the court with the specified ID as deleted in the database
    * by setting its 'deleted' flag to true.
    *
    * @param id the unique identifier of the court to mark as deleted
    * @return ResponseEntity with status OK if the court is successfully marked as deleted,
    *         or ResponseEntity with status BAD_REQUEST if the court with the specified ID is not found,
    *         or ResponseEntity with status INTERNAL_SERVER_ERROR if an exception occurs during the operation
    */
    @Transactional
    @DeleteMapping("/deleteCourtById/{id}")
    public ResponseEntity<HttpStatus> deleteCourt(@PathVariable Long id) {
        try {

            Court court = courtRepository.findById(id);

            if (court == null){
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            }

            court.setDeleted(true);
            ResponseEntity<Court> courtResponse = this.updateCourt(id, court);

            if(courtResponse.equals(new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR))){
                return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
            }

            return new ResponseEntity<>(HttpStatus.OK);
            
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
        
    }

    /**
    * Deletes all courts from the database.
    *
    * This method deletes all courts from the database by iterating over each court
    * and invoking the deleteCourt method to mark them as deleted.
    *
    * @return ResponseEntity with status OK if all courts are successfully deleted,
    *         or ResponseEntity with status NO_CONTENT if there are no courts to delete,
    *         or ResponseEntity with status INTERNAL_SERVER_ERROR if an exception occurs during the operation
    */
    @Transactional
    @DeleteMapping("/deleteAllCourts")
    public ResponseEntity<HttpStatus> deleteAllCourts() {
        try {
            List<Court> courts = courtRepository.findAll();
            if (courts == null){
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }

            for(Court court : courts){
                ResponseEntity<HttpStatus> courtResponse = this.deleteCourt(court.getId());
                if(courtResponse.equals(new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR))){
                    return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
                }
            }

            return new ResponseEntity<>(HttpStatus.OK);
            
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}